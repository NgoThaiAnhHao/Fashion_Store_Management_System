package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.dto.notification.NotificationResponseDto;
import com.student.fashion_store_management_system.model.entity.Notification;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.User;

import java.util.List;

public interface NotificationService {
    Notification createNotification(User user, Order order, String title, String message, String type);
    List<NotificationResponseDto> getNotificationsForUser(User user); // Changed return type
    void markAsRead(Long notificationId);
    long getUnreadNotificationCount(User user);
}
