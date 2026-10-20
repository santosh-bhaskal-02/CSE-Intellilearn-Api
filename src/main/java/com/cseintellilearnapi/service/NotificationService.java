package com.cseintellilearnapi.service;

import com.cseintellilearnapi.entity.Notification;
import com.cseintellilearnapi.exception.BadRequestException;
import com.cseintellilearnapi.exception.ResourceNotFoundException;
import com.cseintellilearnapi.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional(readOnly = true)
    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Notification createNotification(Notification notification) {
        if (notification.getUserId() == null || notification.getTitle() == null || notification.getMessage() == null) {
            throw new BadRequestException("User, title and message are required");
        }
        return notificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }
}
