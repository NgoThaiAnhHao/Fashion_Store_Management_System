package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.model.dto.notification.NotificationResponseDto;
import com.student.fashion_store_management_system.model.entity.Notification;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.OrderDetail;
import com.student.fashion_store_management_system.model.entity.Payment;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.NotificationRepository;
import com.student.fashion_store_management_system.repository.OrderDetailRepository;
import com.student.fashion_store_management_system.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public Notification createNotification(User user, Order order, String title, String message, String type) {
        Notification notification = new Notification();

        notification.setUser(user);
        notification.setOrder(order);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);

        return notificationRepository.save(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(notification -> {
                    Payment payment = paymentRepository
                            .findByOrder(notification.getOrder())
                            .orElse(null);

                    List<OrderDetail> orderDetails = orderDetailRepository
                            .findAllByOrder(notification.getOrder());

                    return NotificationResponseDto.fromEntity(
                            notification,
                            payment,
                            orderDetails
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public long getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }
}