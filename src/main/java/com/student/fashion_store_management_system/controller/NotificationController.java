package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.notification.NotificationResponseDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@RestController // Changed to RestController to return JSON by default
@RequestMapping("/fashion-store/api/notifications") // Changed mapping for API endpoint
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications() {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        List<NotificationResponseDto> notifications = notificationService.getNotificationsForUser(currentUser);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/mark-as-read/{notificationId}") // Changed to PutMapping for update operation
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok(Map.of("message", "Notification marked as read."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error marking notification as read: " + e.getMessage()));
        }
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadNotificationCount() {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        long count = notificationService.getUnreadNotificationCount(currentUser);
        return ResponseEntity.ok(count);
    }
}
