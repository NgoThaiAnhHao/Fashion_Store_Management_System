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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/fashion-store")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public String payment(@ModelAttribute("orderId") long orderId,
                          Model model) {
        Payment payment = new Payment();
        payment.setPaymentMethod(PaymentMethodEnum.COD);

        model.addAttribute("orderId", orderId);
        model.addAttribute("payment", payment);
        return "payment";
    }

    @PostMapping("/payment")
    public String addNew(@ModelAttribute("payment") Payment payment,
                         @RequestParam long orderId,
                         HttpSession session) {

        // Get total amount
        Cart cart = (Cart) session.getAttribute("cart");
        BigDecimal totalAmount = cart.getTotalAmount();

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
