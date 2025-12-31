package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getNotificationsForUser(Long userId);
    void markAllAsRead(Long userId);
}
