package com.cseintellilearnapi.controller;

import com.cseintellilearnapi.entity.Notification;
import com.cseintellilearnapi.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable("user_id") Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        Notification saved = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Notification created successfully",
                "notification_id", saved.getNotificationId()
        ));
    }

    @PutMapping("/{notification_id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable("notification_id") Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Notification marked as read"));
    }
}
