package co.za.neighborlygigs.domain;

import co.za.neighborlygigs.domain.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import co.za.neighborlygigs.domain.*;

@Entity
@Table(name = "application", uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "applicant_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt = LocalDateTime.now();
}
