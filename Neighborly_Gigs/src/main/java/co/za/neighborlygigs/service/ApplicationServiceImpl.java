package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.*;
import co.za.neighborlygigs.domain.enums.TaskStatus;
import co.za.neighborlygigs.factory.ApplicationFactory;
import co.za.neighborlygigs.factory.NotificationFactory;
import co.za.neighborlygigs.repository.ApplicationRepository;
import co.za.neighborlygigs.repository.TaskRepository;
import co.za.neighborlygigs.repository.UserRepository;
import co.za.neighborlygigs.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    private final TaskRepository taskRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public ApplicationServiceImpl(TaskRepository taskRepository,
                                  ApplicationRepository applicationRepository,
                                  UserRepository userRepository,
                                  NotificationRepository notificationRepository, EmailService emailService) {
        this.taskRepository = taskRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.emailService = emailService;
    }

    @Override
    public Application applyToTask(Long taskId, String applicantEmail, String message) {
        //  Validate task is OPEN
        Task task = taskRepository.findByIdAndStatus(taskId, TaskStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Open task not found"));

        // Get applicant by email
        User applicant = userRepository.findByEmail(applicantEmail)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        // Prevent applying to own task
        if (task.getPoster().getEmail().equals(applicantEmail)) {
            throw new RuntimeException("You cannot apply to your own task");
        }

        // Prevent duplicate applications
        if (applicationRepository.findByTask_IdAndApplicant_Id(taskId, applicant.getId()).isPresent()) {
            throw new RuntimeException("You have already applied to this task");
        }

        // Create application
        Application application = ApplicationFactory.createApplication(task, applicant, message);
        Application savedApp = applicationRepository.save(application);

        // Notify task poster
        String applicantName = applicant.getFirstName() + " " + applicant.getLastName();
        Notification notification = NotificationFactory.createNewApplicationNotification(
                task.getPoster(),
                applicantName,
                task.getTitle()
        );
        notificationRepository.save(notification);

        // Send email to poster — safely handle exception
        try {
            emailService.sendTaskApplicationEmail(task.getPoster(), applicant, task.getTitle());
        } catch (MessagingException e) {
            logger.warn("Failed to send application email to {}: {}", task.getPoster().getEmail(), e.getMessage());
        }
        return savedApp;
    }
}