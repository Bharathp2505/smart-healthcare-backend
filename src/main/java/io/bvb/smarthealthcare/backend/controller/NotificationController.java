package io.bvb.smarthealthcare.backend.controller;


import io.bvb.smarthealthcare.backend.model.NotificationResponse;
import io.bvb.smarthealthcare.backend.service.NotificationService;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/notifications", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> allNotifications() {
        return notificationService.getUserNotifications(CurrentUserData.getUser().getId());
    }

    @GetMapping("/{userId}")
    public List<NotificationResponse> getUserNotifications(@PathVariable Long userId) {
        return notificationService.getUserNotifications(userId);
    }
}

