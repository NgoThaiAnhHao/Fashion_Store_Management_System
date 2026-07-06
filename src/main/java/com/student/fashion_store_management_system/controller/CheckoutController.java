package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.order.OrderCreateDto;
import com.student.fashion_store_management_system.model.entity.Cart;
import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.service.OrderDetailService;
import com.student.fashion_store_management_system.service.OrderService;
import com.student.fashion_store_management_system.service.ProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class CheckoutController {

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final ProductService productService;

    @GetMapping("/checkout")
    public String checkout(HttpSession session,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/fashion-store/cart";
        }

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

        // Check cart null / empty
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty!");
            return "redirect:/fashion-store/cart";
        }

        // Check validation error
        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            return "checkout";
        }

        // Check stock quantity from product
        for (CartItem item : cart.getItems()) {
            Product product = productService.findById(item.getProduct().getProductId());

            if (product == null) {
                model.addAttribute("error", "PRODUCT NOT FOUND");
                model.addAttribute("cart", cart);
                return "checkout";
            }

            int totalQuantity = item.getMember1Quantity() + item.getMember2Quantity();

            if (product.getStockQuantity() < totalQuantity) {
                model.addAttribute(
                        "error",
                        String.format("STOCK QUANTITY FOR PRODUCT '%s' NOT ENOUGH", product.getName())
                );
                model.addAttribute("cart", cart);
                return "checkout";
            }
        }

        // Get total amount
        BigDecimal totalAmount = cart.getTotalAmount();

        // Save Order to Database
        Order savedOrder = orderService.addNew(orderCreateDto, totalAmount);

        // Save OrderDetail(CartItem) to Database
        orderDetailService.addNew(cart.getItems(), savedOrder);

        // Redirect with query param instead of flashAttribute
        return "redirect:/fashion-store/payment?orderId=" + savedOrder.getOrderId();
    }
}