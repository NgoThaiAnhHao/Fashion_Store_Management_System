package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.enums.PaymentMethodEnum;
import com.student.fashion_store_management_system.enums.PaymentStatusEnum;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.mapper.OrderMapper;
import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.OrderRepository;
import com.student.fashion_store_management_system.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;
    private final NotificationService notificationService;
    private final PaymentRepository paymentRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findMyOrders() {
        User currentUser = authenticationService.getCurrentUser();
        return orderRepository.findAllByOrderedBy(currentUser);
    }

    @Override
    public Order findById(long orderId) {
        return orderRepository
                .findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("ORDER NOT FOUND")
                );
    }

    @Override
    @Transactional
    public Order addNew(OrderCreateDto orderCreateDto, BigDecimal totalAmount) {
        User currentUser = authenticationService.getCurrentUser();

        return orderRepository.save(
                OrderMapper.toEntity(orderCreateDto, currentUser, totalAmount)
        );
    }

    @Override
    @Transactional
    public void updateStatus(long id, OrderStatusEnum status, String rejectReason) {
        Order order = findById(id);
        order.setStatus(status);

        if (status == OrderStatusEnum.LOGO_REJECTED) {
            order.setRejectReason(rejectReason);
        } else {
            order.setRejectReason(null);
        }

        orderRepository.save(order);

        User customer = order.getOrderedBy();

        Payment payment = paymentRepository.findByOrder(order).orElse(null);

        boolean isOnlinePaymentPending =
                payment != null
                        && payment.getPaymentMethod() == PaymentMethodEnum.CARD
                        && payment.getPaymentStatus() == PaymentStatusEnum.PENDING;

        String title;
        String message;
        String type;

        if (status == OrderStatusEnum.CONFIRMED && isOnlinePaymentPending) {

            title = "Payment Required";
            message = "Your order has been confirmed. Please complete your online payment.";
            type = "PAYMENT_REQUIRED";

        } else if (status == OrderStatusEnum.CONFIRMED) {

            title = "Order Confirmed";
            message = "Your logo has been approved. Your order is now confirmed and will proceed to production.";
            type = "ORDER_CONFIRMED";

        } else if (status == OrderStatusEnum.LOGO_REJECTED) {

            title = "Logo Rejected";
            message = "Your logo has been rejected. Please upload another logo.";

            if (rejectReason != null && !rejectReason.trim().isEmpty()) {
                message += " Reason: " + rejectReason;
            }

            type = "LOGO_REJECTED";

        } else if (status == OrderStatusEnum.SHIPPING) {

            title = "Order Shipping";
            message = "Your order is now being shipped.";
            type = "ORDER_SHIPPING";

        } else if (status == OrderStatusEnum.COMPLETED) {

            title = "Order Completed";
            message = "Your order has been completed. Thank you for shopping with us.";
            type = "ORDER_COMPLETED";

        } else if (status == OrderStatusEnum.CANCELLED) {

            title = "Order Cancelled";
            message = "Your order has been cancelled.";
            type = "ORDER_CANCELLED";

        } else if (status == OrderStatusEnum.PENDING) {

            title = "Order Pending";
            message = "Your order is pending review.";
            type = "ORDER_PENDING";

        } else {

            title = "Order Updated";
            message = "Your order status has been updated to " + status + ".";
            type = "ORDER_UPDATED";

        }

        notificationService.createNotification(customer, order, title, message, type);
    }

    @Override
    public List<Order> findByUserFullName(String fullName) {
        return orderRepository.findByOrderedBy_FullNameContainingIgnoreCase(fullName);
    }
}