// co.za.neighborlygigs.service.ReviewServiceImpl.java

package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.*;
import co.za.neighborlygigs.domain.enums.*;
import co.za.neighborlygigs.repository.ReviewRepository;
import co.za.neighborlygigs.repository.TaskRepository;
import co.za.neighborlygigs.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(TaskRepository taskRepository,
                             UserRepository userRepository,
                             ReviewRepository reviewRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review submitReview(Long taskId, String reviewerEmail, String revieweeEmail, int rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        // Load task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Ensure task is completed
        if (task.getStatus() != TaskStatus.COMPLETED) {
            throw new RuntimeException("Reviews can only be submitted for completed tasks");
        }

        // Load users
        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        User reviewee = userRepository.findByEmail(revieweeEmail)
                .orElseThrow(() -> new RuntimeException("Reviewee not found"));

        // Ensure both users were involved in the task
        if (!isUserInvolvedInTask(reviewer, task) || !isUserInvolvedInTask(reviewee, task)) {
            throw new RuntimeException("Both reviewer and reviewee must be involved in the task");
        }

        // Prevent duplicate reviews
        if (reviewRepository.existsByTask_IdAndReviewer_IdAndReviewee_Id(taskId, reviewer.getId(), reviewee.getId())) {
            throw new RuntimeException("You have already reviewed this user for this task");
        }

        // Create review
        Review review = new Review();
        review.setTask(task);
        review.setReviewer(reviewer);
        review.setReviewee(reviewee);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForTask(Long taskId) {
        return reviewRepository.findByTask_Id(taskId);
    }

    @Override
    public List<Review> getReviewsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reviewRepository.findByReviewee_Id(user.getId());
    }

    private boolean isUserInvolvedInTask(User user, Task task) {
        return task.getPoster().getId().equals(user.getId()) ||
                (task.getAssignedTo() != null && task.getAssignedTo().getId().equals(user.getId()));
    }
}