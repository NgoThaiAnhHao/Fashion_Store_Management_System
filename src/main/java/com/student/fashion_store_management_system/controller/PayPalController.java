package com.student.fashion_store_management_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.enums.PaymentMethodEnum;
import com.student.fashion_store_management_system.enums.PaymentStatusEnum;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.PaymentRepository;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.OrderService;
import com.student.fashion_store_management_system.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/fashion-store")
@RequiredArgsConstructor
public class PayPalController {

    private final OrderService orderService;
    private final PaymentRepository paymentRepository;
    private final AuthenticationService authenticationService;
    private final PayPalService payPalService;

    @Value("${paypal.client-id}")
    private String paypalClientId;

    @GetMapping("/paypal/checkout/{orderId}")
    public String paypalCheckout(@PathVariable long orderId, Model model) {
        Order order = orderService.findById(orderId);

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT NOT FOUND"));

        validateCustomerOwnsOrder(order);

        if (payment.getPaymentStatus() == PaymentStatusEnum.PAID) {
            return "redirect:/fashion-store/order-detail/" + order.getOrderId();
        }

        validateOrderCanBePaid(order, payment);

        model.addAttribute("orderId", order.getOrderId());
        model.addAttribute("amount", payment.getTotalAmount());
        model.addAttribute("paypalClientId", paypalClientId);

        return "paypal-payment";
    }

    @PostMapping("/api/paypal/orders/{orderId}")
    @ResponseBody
    public ResponseEntity<?> createPayPalOrder(@PathVariable long orderId) {
        Order order = orderService.findById(orderId);

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT NOT FOUND"));

        validateCustomerOwnsOrder(order);

        if (payment.getPaymentStatus() == PaymentStatusEnum.PAID) {
            return ResponseEntity.ok(Map.of(
                    "status", "ALREADY_PAID",
                    "redirectUrl", "/fashion-store/order-detail/" + order.getOrderId()
            ));
        }

        validateOrderCanBePaid(order, payment);

        String paypalOrderId = payPalService.createOrder(order, payment);

        payment.setPaypalOrderId(paypalOrderId);
        paymentRepository.save(payment);

        return ResponseEntity.ok(Map.of(
                "id", paypalOrderId
        ));
    }

    @PostMapping("/api/paypal/orders/{orderId}/capture/{paypalOrderId}")
    @ResponseBody
    public ResponseEntity<?> capturePayPalOrder(@PathVariable long orderId,
                                                @PathVariable String paypalOrderId) {
        Order order = orderService.findById(orderId);

        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("PAYMENT NOT FOUND"));

        validateCustomerOwnsOrder(order);

        if (payment.getPaymentStatus() == PaymentStatusEnum.PAID) {
            return ResponseEntity.ok(Map.of(
                    "status", "COMPLETED",
                    "redirectUrl", "/fashion-store/order-detail/" + order.getOrderId()
            ));
        }

        validateOrderCanBePaid(order, payment);

        JsonNode result = payPalService.captureOrder(paypalOrderId);
        String status = result.path("status").asText();

        if ("COMPLETED".equals(status)) {
            payment.setPaymentStatus(PaymentStatusEnum.PAID);

            JsonNode captures = result
                    .path("purchase_units")
                    .path(0)
                    .path("payments")
                    .path("captures");

            if (captures.isArray() && captures.size() > 0) {
                payment.setPaypalCaptureId(captures.get(0).path("id").asText());
            }

            paymentRepository.save(payment);

            return ResponseEntity.ok(Map.of(
                    "status", "COMPLETED",
                    "redirectUrl", "/fashion-store/order-detail/" + order.getOrderId()
            ));
        }

        payment.setPaymentStatus(PaymentStatusEnum.FAILED);
        paymentRepository.save(payment);

        return ResponseEntity.badRequest().body(Map.of(
                "status", status,
                "message", "Payment was not completed."
        ));
    }

    private void validateCustomerOwnsOrder(Order order) {
        User currentUser = authenticationService.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("You must login to pay this order.");
        }

        if (order.getOrderedBy() == null
                || order.getOrderedBy().getUserId() != currentUser.getUserId()) {
            throw new RuntimeException("You are not allowed to pay this order.");
        }
    }

    private void validateOrderCanBePaid(Order order, Payment payment) {
        if (order.getStatus() != OrderStatusEnum.CONFIRMED) {
            throw new RuntimeException("This order is not confirmed yet.");
        }

        if (payment.getPaymentMethod() != PaymentMethodEnum.CARD) {
            throw new RuntimeException("This order is not an online payment order.");
        }

        if (payment.getPaymentStatus() != PaymentStatusEnum.PENDING) {
            throw new RuntimeException("This payment is not pending.");
        }
    }
}