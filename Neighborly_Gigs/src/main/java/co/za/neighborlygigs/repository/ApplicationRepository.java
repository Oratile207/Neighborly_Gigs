package co.za.neighborlygigs.repository;

import co.za.neighborlygigs.domain.Application;
import co.za.neighborlygigs.domain.enums.ApplicationStatus;
import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByTask_Id(Long taskId);
    List<Application> findByApplicant_Id(Long userId);
    Optional<Application> findByTask_IdAndApplicant_Id(Long taskId, Long applicantId);

    // Count pending applications for a task
    long countByTask_IdAndStatus(Long taskId, ApplicationStatus status);
}
