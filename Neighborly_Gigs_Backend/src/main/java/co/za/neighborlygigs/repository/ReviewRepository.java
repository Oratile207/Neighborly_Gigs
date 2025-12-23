package co.za.neighborlygigs.repository;

import co.za.neighborlygigs.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTask_Id(Long taskId);
    List<Review> findByReviewee_Id(Long userId);
    boolean existsByTask_IdAndReviewer_IdAndReviewee_Id(Long taskId, Long reviewerId, Long revieweeId);

}