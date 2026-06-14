package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.PaymentMethodEnum;
import com.student.fashion_store_management_system.enums.PaymentStatusEnum;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    List<Payment> findAll();

    Payment addNew(Payment payment, long orderId, BigDecimal totalAmount);

    Payment findByOrder(Order order);

    Payment findById(long paymentId);

    void updateStatus(long paymentId, PaymentStatusEnum paymentStatusEnum);
}
