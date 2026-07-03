package com.student.fashion_store_management_system.model.entity;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @CreationTimestamp
    @Column(name = "ordered_date", nullable = false, updatable = false)
    private LocalDateTime orderedDate;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false)
    private String receiverPhone;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "reject_reason")
    private String rejectReason;

    @ManyToOne
    @JoinColumn(name = "ordered_by")
    private User orderedBy;

    @PrePersist
    public void onCreate() {
        this.status = OrderStatusEnum.PENDING;
    }

    public Order(BigDecimal totalAmount, String shippingAddress, String receiverName, String receiverPhone, String city, String zipcode, User orderedBy) {
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.city = city;
        this.zipcode = zipcode;
        this.orderedBy = orderedBy;
    }

    public String getFormattedOrderedAt() {
        return orderedDate.format(
                DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm")
        );
    }
}