package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Application;
import co.za.neighborlygigs.domain.User;

public interface ApplicationService {
    Application applyToTask(Long taskId, String applicantEmail, String message);
}
