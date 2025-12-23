package co.za.neighborlygigs.repository;

import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.enums.TaskStatus;
import co.za.neighborlygigs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByPoster_Id(Long userId);
    List<Task> findByAssignedTo_Id(Long userId);
    List<Task> findByStatus(TaskStatus status);

    // Open tasks (for job board)
    List<Task> findByStatusOrderByIdDesc(TaskStatus status);

    // Tasks assigned to a user that are completed
    List<Task> findByAssignedTo_IdAndStatus(Long userId, TaskStatus status);

    // For application validation: ensure task is OPEN
//    Task findByIdAndStatus(Long id, TaskStatus status);
    Optional<Task> findByIdAndStatus(Long id, TaskStatus status);

}