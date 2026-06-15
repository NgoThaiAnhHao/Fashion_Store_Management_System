package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.PaymentMethodEnum;
import com.student.fashion_store_management_system.enums.PaymentStatusEnum;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;
import com.student.fashion_store_management_system.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    @Transactional
    public Payment addNew(Payment payment, long orderId, BigDecimal totalAmount) {
        // Finding Order by orderId
        Order order = orderService.findById(orderId);

        // Set Order ID and Total Amount for Payment
        payment.setOrder(order);
        payment.setTotalAmount(totalAmount);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment findByOrder(Order order) {
        return paymentRepository.findByOrder(order)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PAYMENT NOT FOUND")
                );
    }

    @Override
    public Payment findById(long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PAYMENT NOT FOUND")
                );
    }

    @Override
    @Transactional
    public void updateStatus(long paymentId, PaymentStatusEnum paymentStatusEnum) {
        Payment payment = findById(paymentId);
        payment.setPaymentStatus(paymentStatusEnum);
        paymentRepository.save(payment);
    }
}
