package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.enums.*;
import co.za.neighborlygigs.domain.*;
import co.za.neighborlygigs.factory.TaskFactory;
import co.za.neighborlygigs.factory.NotificationFactory;
import co.za.neighborlygigs.repository.*;
import co.za.neighborlygigs.util.GeoCodingUtil;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final TransactionService transactionService;
    private final EmailService emailService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           ApplicationRepository applicationRepository,
                           UserRepository userRepository,
                           NotificationRepository notificationRepository,
                           TransactionService transactionService, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.transactionService = transactionService;
        this.emailService = emailService;

    }

    @Override
    public Task createTask(String title, String description, TaskCategory category,
                           BigDecimal budget, String address, String requirements, String posterEmail) {
        User poster = userRepository.findByEmail(posterEmail)
                .orElseThrow(() -> new RuntimeException("Poster not found"));
        String formattedAddress = GeoCodingUtil.formatAddressForGeocoding(address);
        Task task = TaskFactory.createTask(title, description, category, budget, formattedAddress, requirements, poster);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllOpenTasks() {
        return taskRepository.findByStatusOrderByIdDesc(TaskStatus.OPEN);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    public Task assignTask(Long taskId, String applicantEmail, String posterEmail) {
        Task task = getTaskById(taskId);
        User poster = userRepository.findByEmail(posterEmail)
                .orElseThrow(() -> new RuntimeException("Poster not found"));
        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        if (!task.getPoster().getId().equals(poster.getId())) {
            throw new RuntimeException("Only the task poster can assign this task");
        }

        task.setAssignedTo(applicant);
        task.setStatus(TaskStatus.ASSIGNED);

        Application application = applicationRepository.findByTask_IdAndApplicant_Id(taskId, applicant.getId())
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(ApplicationStatus.ACCEPTED);

        notificationRepository.save(
                NotificationFactory.createTaskAssignedNotification(applicant, task.getTitle())
        );

        // Send email — safely handle exception
        try {
            emailService.sendTaskAssignedEmail(applicant, task.getTitle());
        } catch (MessagingException e) {
            logger.warn("Failed to send assignment email to {}: {}", applicant.getEmail(), e.getMessage());
            // Do NOT fail the transaction — email is secondary
        }
        return taskRepository.save(task);
    }

    @Override
    public Task completeTask(Long taskId, String currentUserEmail) {
        Task task = getTaskById(taskId);
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!task.getPoster().getId().equals(currentUser.getId()) &&
                !task.getAssignedTo().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Only involved parties can complete this task");
        }

        task.setStatus(TaskStatus.COMPLETED);
        task.setCompletedAt(java.time.LocalDateTime.now());
        transactionService.createTransaction(task);

        notificationRepository.save(
                NotificationFactory.createTaskCompletedNotification(task.getPoster(), task.getTitle())
        );
        notificationRepository.save(
                NotificationFactory.createTaskCompletedNotification(task.getAssignedTo(), task.getTitle())
        );

        // Send emails — safely handle exceptions
        try {
            emailService.sendTaskCompletedEmail(task.getPoster(), task.getTitle(), false);
        } catch (MessagingException e) {
            logger.warn("Failed to send completion email to poster {}: {}", task.getPoster().getEmail(), e.getMessage());
        }

        try {
            emailService.sendTaskCompletedEmail(task.getAssignedTo(), task.getTitle(), true);
        } catch (MessagingException e) {
            logger.warn("Failed to send completion email to completer {}: {}", task.getAssignedTo().getEmail(), e.getMessage());
        }

        // Notify both parties to leave reviews
        notificationRepository.save(
                NotificationFactory.createReviewReminderNotification(task.getPoster(), task.getAssignedTo(), task.getTitle())
        );
        notificationRepository.save(
                NotificationFactory.createReviewReminderNotification(task.getAssignedTo(), task.getPoster(), task.getTitle())
        );
        return taskRepository.save(task);
    }
}