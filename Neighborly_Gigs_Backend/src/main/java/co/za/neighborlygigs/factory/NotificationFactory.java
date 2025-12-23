package co.za.neighborlygigs.factory;

import co.za.neighborlygigs.domain.Notification;
import co.za.neighborlygigs.domain.User;

public class NotificationFactory {
    public static Notification createTaskAssignedNotification(User recipient, String taskTitle) {
        return Notification.builder()
                .recipient(recipient)
                .title("You've been assigned a task!")
                .message("You’ve been selected to complete: \"" + taskTitle + "\"")
                .build();
    }

    public static Notification createNewApplicationNotification(User recipient, String applicantName, String taskTitle) {
        return Notification.builder()
                .recipient(recipient)
                .title("New application for your task")
                .message(applicantName + " applied to \"" + taskTitle + "\"")
                .build();
    }

    public static Notification createTaskCompletedNotification(User recipient, String taskTitle) {
        return Notification.builder()
                .recipient(recipient)
                .title("Task completed!")
                .message("The task \"" + taskTitle + "\" has been marked as complete.")
                .build();
    }
    public static Notification createReviewReminderNotification(
            User recipient, User otherParty, String taskTitle) {
        String otherName = otherParty.getFirstName();
        return Notification.builder()
                .recipient(recipient)
                .title("Review your recent gig!")
                .message(String.format(
                        "%s helped you with “%s” — please take a moment to leave a review. " +
                                "Your feedback helps build trust in our community! 🌟",
                        otherName, taskTitle))
                .build();
    }

    public static Notification createEmailVerifiedNotification(User recipient) {
        return Notification.builder()
                .recipient(recipient)
                .title("Welcome to Neighborly Gigs!")
                .message("Your email has been verified. " +
                        "You can now post tasks, apply to gigs, and start earning or getting help!")
                .build();
    }
}
