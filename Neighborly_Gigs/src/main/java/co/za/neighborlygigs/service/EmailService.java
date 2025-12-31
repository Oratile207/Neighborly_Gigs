package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTaskApplicationEmail(User taskPoster, User applicant, String taskTitle) throws MessagingException {
        String subject = "New Application for Your Task: " + taskTitle;
        String text = String.format("""
                Hi %s,

                %s %s has applied to your task: "%s".

                You can review their profile and assign the task from your dashboard.

                Thank you for using Neighborly Gigs — building trust among neighbors! 🌟

                — The Neighborly Gigs Team
                """,
                taskPoster.getFirstName(),
                applicant.getFirstName(),
                applicant.getLastName(),
                taskTitle
        );

        sendEmail(taskPoster.getEmail(), subject, text);
    }

    public void sendTaskAssignedEmail(User applicant, String taskTitle) throws MessagingException {
        String subject = "You’ve Been Assigned to a Task!";
        String text = String.format("""
                Hi %s,

                Great news! You’ve been selected to complete the task: "%s".

                Please coordinate with the task poster and get started soon.

                Thank you for being part of our community! 💪

                — The Neighborly Gigs Team
                """,
                applicant.getFirstName(),
                taskTitle
        );

        sendEmail(applicant.getEmail(), subject, text);
    }

    public void sendTaskCompletedEmail(User user, String taskTitle, boolean isCompleter) throws MessagingException {
        String subject = "Task Completed: " + taskTitle;
        String text;
        if (isCompleter) {
            text = String.format("""
                    Hi %s,

                    Your completion of "%s" has been confirmed!

                    The platform will process your payout (80%% of the task fee) shortly.
                    Thank you for your service! 🙌

                    — The Neighborly Gigs Team
                    """, user.getFirstName(), taskTitle);
        } else {
            text = String.format("""
                    Hi %s,

                    The task "%s" has been marked as complete.

                    A 20%% platform fee has been deducted, and the helper has been paid.
                    Please leave a review to help build community trust.

                    — The Neighborly Gigs Team
                    """, user.getFirstName(), taskTitle);
        }

        sendEmail(user.getEmail(), subject, text);
    }

    private void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false); // false = plain text (not HTML)
        helper.setFrom("no-reply@neighborlygigs.co.za"); // or your verified sender
        mailSender.send(message);
    }
}
