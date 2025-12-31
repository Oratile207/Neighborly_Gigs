package co.za.neighborlygigs.service;

import co.za.neighborlygigs.domain.Notification;
import co.za.neighborlygigs.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByRecipient_IdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }
}