package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Review;
import co.za.neighborlygigs.domain.User;

import java.util.List;

public interface ReviewService {
    /**
     * Submit a review from reviewer → reviewee for a completed task.
     * Enforces:
     * - Task must be COMPLETED
     * - Reviewer and reviewee must be involved in the task
     * - Only one review per direction per task
     */
    Review submitReview(Long taskId, String reviewerEmail, String revieweeEmail, int rating, String comment);

     // Get all reviews for a task (both directions)
    List<Review> getReviewsForTask(Long taskId);

    //Get all reviews received by a user (for profile page)
    List<Review> getReviewsForUser(String userEmail);
}
