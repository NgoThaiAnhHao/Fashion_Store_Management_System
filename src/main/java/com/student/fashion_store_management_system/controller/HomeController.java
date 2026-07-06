package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.*;
import com.student.fashion_store_management_system.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO: FIX PROFILE PHOTO,
 * BUG Avatar at HOME>layout.html
 * Check user.avatar
 */
@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final CategoryService categoryService;
    private final NotificationService notificationService; // Inject NotificationService

    @GetMapping("/dashboard")
    public String home(Model model, HttpSession session) {
        // Log current user for debug
        User user = authenticationService.getCurrentUser();

        session.setAttribute("currentUser", user);

        // Add unread notification count to model
        if (user != null) {
            model.addAttribute("unreadNotificationCount", notificationService.getUnreadNotificationCount(user));
        }


        // Process for Admin Or Manager
        if (user != null &&
                ("ROLE_ADMIN".equals(user.getRoles().getRoleName().toString()) ||
                "ROLE_MANAGER".equals(user.getRoles().getRoleName().toString()))
        ) {

            // Find all objects
            List<UserResponseDto> users = userService.findAll();
            List<UserResponseDto> managers = userService.findAllByRole(RoleNameEnum.ROLE_MANAGER);
            List<UserResponseDto> customers = userService.findAllByRole(RoleNameEnum.ROLE_CUSTOMER);
            List<Product> products = productService.findAll();
            List<Payment> payments = paymentService.findAll();
            List<Category> categories = categoryService.findAll();
            List<Order> orders = orderService.findAll();

            // Get revenue
            BigDecimal revenue = BigDecimal.ZERO;
            for (var order : orders) {
                revenue = revenue.add(order.getTotalAmount());
            }

            // Set attribute
            model.addAttribute("totalUsers", users.size());
            model.addAttribute("totalManagers", managers.size());
            model.addAttribute("totalCustomers", customers.size());
            model.addAttribute("totalProducts", products.size());
            model.addAttribute("totalPayments", payments.size());
            model.addAttribute("totalCategories", categories.size());
            model.addAttribute("totalOrders", orders.size());
            model.addAttribute("revenue", revenue);

            return "/admin/admin-dashboard";
        }

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "dashboard";
    }

    @GetMapping("/denied-page")
    public String deniedPage() {
        return "denied-page";
    }

}
