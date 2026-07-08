package com.student.fashion_store_management_system.model.dto.notification;

import com.student.fashion_store_management_system.enums.PaymentStatusEnum;
import com.student.fashion_store_management_system.model.entity.Notification;
import com.student.fashion_store_management_system.model.entity.OrderDetail;
import com.student.fashion_store_management_system.model.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {

    private Long id;
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;
    private Long orderId;

    private boolean paymentRequired;
    private boolean paymentPaid;
    private String paymentUrl;
    private String orderDetailUrl;
    private String productNames;

    public String getFormattedCreatedAt() {
        return createdAt.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
        );
    }

    public static NotificationResponseDto fromEntity(Notification notification,
                                                     Payment payment,
                                                     List<OrderDetail> orderDetails) {
        NotificationResponseDto dto = new NotificationResponseDto();

        Long orderId = notification.getOrder().getOrderId();

        String productNames = orderDetails == null
                ? ""
                : orderDetails.stream()
                  .filter(orderDetail -> orderDetail.getProduct() != null)
                  .map(orderDetail -> orderDetail.getProduct().getName())
                  .distinct()
                  .collect(Collectors.joining(", "));

        boolean isPaymentNotification =
                "PAYMENT_REQUIRED".equals(notification.getType())
                        || "PAYMENT_SUCCESS".equals(notification.getType());

        boolean isPaid =
                payment != null
                        && payment.getPaymentStatus() == PaymentStatusEnum.PAID;

        boolean needPayment =
                "PAYMENT_REQUIRED".equals(notification.getType())
                        && payment != null
                        && payment.getPaymentStatus() == PaymentStatusEnum.PENDING;

        dto.setId(notification.getId());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setOrderId(orderId);

        dto.setProductNames(productNames);
        dto.setOrderDetailUrl("/fashion-store/order-detail/" + orderId);

        dto.setPaymentRequired(needPayment);
        dto.setPaymentPaid(isPaymentNotification && isPaid);

        if (needPayment) {
            dto.setTitle(notification.getTitle());
            dto.setMessage(notification.getMessage());
            dto.setType("PAYMENT_REQUIRED");
            dto.setPaymentUrl("/fashion-store/paypal/checkout/" + orderId);
        } else if (isPaymentNotification && isPaid) {
            dto.setTitle("Payment Completed");
            dto.setMessage("You have successfully paid for: " + productNames);
            dto.setType("PAYMENT_SUCCESS");
            dto.setPaymentUrl(null);
        } else {
            dto.setTitle(notification.getTitle());
            dto.setMessage(notification.getMessage());
            dto.setType(notification.getType());
            dto.setPaymentUrl(null);
        }

        return dto;
    }
}