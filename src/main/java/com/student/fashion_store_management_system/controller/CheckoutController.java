package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.*;
import com.student.fashion_store_management_system.service.OrderDetailService;
import com.student.fashion_store_management_system.service.OrderService;
import com.student.fashion_store_management_system.service.PaymentService;
import com.student.fashion_store_management_system.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("order", new OrderCreateDto());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String createOrderDetail(@Valid @ModelAttribute("order") OrderCreateDto orderCreateDto,
                          BindingResult bindingResult,
                          HttpSession session,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        // Check stock quantity from product
        for (CartItem item : cart.getItems()) {
                // Get product from database
                Product product = productService.findById(item.getProduct().getProductId());
                if (product.getStockQuantity() < item.getPairQuantity() * 2) {
                    model.addAttribute("error", String.format("STOCK QUANTITY FOR PRODUCT '%s' NOT ENOUGH", product.getName()));
                    return "checkout";
                }
        };

        // Get total amount
        BigDecimal totalAmount = cart.getTotalAmount();

        // Saved Order to Database
        Order savedOrder = orderService.addNew(orderCreateDto, totalAmount);

        // Saved OrderDetail(CardItem) to Database
        orderDetailService.addNew(cart.getItems(), savedOrder);
        redirectAttributes.addFlashAttribute("orderId", savedOrder.getOrderId());
        return "redirect:/fashion-store/payment";
    }



}
