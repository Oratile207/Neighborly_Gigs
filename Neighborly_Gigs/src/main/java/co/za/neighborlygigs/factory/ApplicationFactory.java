package co.za.neighborlygigs.factory;

import co.za.neighborlygigs.domain.Application;
import co.za.neighborlygigs.domain.enums.ApplicationStatus;
import co.za.neighborlygigs.domain.Task;
import co.za.neighborlygigs.domain.User;

public class ApplicationFactory {

    public static Application createApplication(Task task, User applicant, String message) {
        return Application.builder()
                .task(task)
                .applicant(applicant)
                .message(message)
                .status(ApplicationStatus.PENDING)
                .build();
    }

    public static Application createApplication(Task task, User applicant) {
        return createApplication(task, applicant, null);
    }
}