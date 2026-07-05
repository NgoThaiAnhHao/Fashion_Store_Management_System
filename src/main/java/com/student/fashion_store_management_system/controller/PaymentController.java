package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.PaymentMethodEnum;
import com.student.fashion_store_management_system.enums.PaymentStatusEnum;
import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.Cart;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;
import com.student.fashion_store_management_system.service.OrderService;
import com.student.fashion_store_management_system.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payment")
    public String payment(@ModelAttribute("orderId") long orderId,
                          Model model,
                          HttpSession session) {
        Payment payment = new Payment();

        Cart sessionCart = (Cart) session.getAttribute("cart");
        boolean hasLogo = false;
        if (sessionCart != null && sessionCart.getItems() != null) {
            hasLogo = sessionCart.getItems()
                .stream()
                .anyMatch(item ->
                    (item.getCustomLogoText() != null && !item.getCustomLogoText().trim().isEmpty())
                    || (item.getCustomLogoImageUrl() != null && !item.getCustomLogoImageUrl().trim().isEmpty())
                );
        }

        model.addAttribute("hasLogo", hasLogo);

        if (hasLogo) {
            payment.setPaymentMethod(PaymentMethodEnum.CARD);
        } else {
            payment.setPaymentMethod(PaymentMethodEnum.COD);
        }

        model.addAttribute("orderId", orderId);
        model.addAttribute("payment", payment);
        return "payment";
    }

    @PostMapping("/payment")
    public String addNew(@ModelAttribute("payment") Payment payment,
                         @RequestParam long orderId,
                         HttpSession session) {

        Cart sessionCart = (Cart) session.getAttribute("cart");
        boolean hasLogo = false;
        if (sessionCart != null && sessionCart.getItems() != null) {
            hasLogo = sessionCart.getItems()
                .stream()
                .anyMatch(item ->
                    (item.getCustomLogoText() != null && !item.getCustomLogoText().trim().isEmpty())
                    || (item.getCustomLogoImageUrl() != null && !item.getCustomLogoImageUrl().trim().isEmpty())
                );
        }

        // Backend Validation
        if (hasLogo && payment.getPaymentMethod() == PaymentMethodEnum.COD) {
            throw new IllegalArgumentException(
                "Orders with custom logos require 100% online payment."
            );
        }

        // Get total amount
        BigDecimal totalAmount = sessionCart.getTotalAmount();

        // Saving to database
        paymentService.addNew(payment, orderId, totalAmount);
        return "redirect:/fashion-store/confirmed";
    }

    @PostMapping("/payment/update-status/{paymentId}")
    public String updateStatus(@PathVariable long paymentId,
                               @RequestParam("orderId") long orderId,
                               @RequestParam("paymentStatus") PaymentStatusEnum paymentStatusEnum) {
        paymentService.updateStatus(paymentId, paymentStatusEnum);
        return "redirect:/fashion-store/order-detail/" + orderId;
    }
}
