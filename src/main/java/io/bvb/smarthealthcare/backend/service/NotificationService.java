package io.bvb.smarthealthcare.backend.service;


import io.bvb.smarthealthcare.backend.entity.Notification;
import io.bvb.smarthealthcare.backend.model.NotificationResponse;
import io.bvb.smarthealthcare.backend.repository.NotificationRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MessageSource messageSource;

    public NotificationService(NotificationRepository notificationRepository, MessageSource messageSource) {
        this.notificationRepository = notificationRepository;
        this.messageSource = messageSource;
    }

    public List<NotificationResponse> getNotifications() {
        return convertNotificationsToResponse(notificationRepository.findAll());
    }

    // Send Notification
    public void sendNotification(Long userId, String messageKey, Object[] params) {
        String message = messageSource.getMessage(messageKey, params, Locale.ENGLISH);
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setDate(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    // Get Notifications by UserId
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return convertNotificationsToResponse(notificationRepository.findByUserId(userId));
    }

    private List<NotificationResponse> convertNotificationsToResponse(List<Notification> notifications) {
        return notifications.stream().map(notification -> {
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setId(notification.getId());
            notificationResponse.setUserId(notification.getUserId());
            notificationResponse.setMessage(notification.getMessage());
            notificationResponse.setDate(notification.getDate());
            return notificationResponse;
        }).collect(Collectors.toList());
    }
}

