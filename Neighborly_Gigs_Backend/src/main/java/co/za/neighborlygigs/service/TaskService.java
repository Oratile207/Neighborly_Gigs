package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.enums.TaskCategory;
import co.za.neighborlygigs.domain.User;

import java.math.BigDecimal;
import java.util.List;

public interface TaskService {
    Task createTask(String title, String description, TaskCategory category,
                    BigDecimal budget, String address, String requirements, String posterEmail);
    List<Task> getAllOpenTasks();
    Task getTaskById(Long id);
    Task assignTask(Long taskId, String applicantEmail, String posterEmail);
    Task completeTask(Long taskId, String currentUserEmail);
}