package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.OrderStatusEnum;
import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.OrderDetail;
import com.student.fashion_store_management_system.model.entity.Payment;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.OrderDetailService;
import com.student.fashion_store_management_system.service.OrderService;
import com.student.fashion_store_management_system.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fashion-store")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final OrderDetailService orderDetailService;
    private final AuthenticationService authenticationService;

    public OrderController(OrderService orderService, PaymentService paymentService, OrderDetailService orderDetailService, AuthenticationService authenticationService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.orderDetailService = orderDetailService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/orders")
    public String findAll(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "/admin/order/order-management";
    }

    @GetMapping("/my-orders")
    public String findMyOrders(Model model) {
        // Find All Orders
        List<Order> orders = orderService.findMyOrders();
        model.addAttribute("orders", orders);
        return "my-orders";
    }

    @PostMapping("/orders/update-status/{orderId}")
    public String updateOrderStatus(@PathVariable long orderId,
                                    @RequestParam OrderStatusEnum status) {
        orderService.updateStatus(orderId, status);
        return "redirect:/fashion-store/orders";
    }

    @GetMapping("/order-detail/{orderId}")
    public String showOrderDetailsByOrderId(@PathVariable long orderId,
                                            Model model) {
        // Find Order by orderId
        Order order = orderService.findById(orderId);

        // Find Payment by Order
        Payment payment = paymentService.findByOrder(order);

        // Find all Order Details by Order
        List<OrderDetail> orderDetails = orderDetailService.findAllByOrder(order);

        // Set attribute
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("order", order);
        model.addAttribute("payment", payment);

        // Get current user
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser != null && "ROLE_ADMIN".equals(currentUser.getRoles().getRoleName().toString())) {
            return "/admin/order-detail/order-detail-management";
        }

        return "my-order-details";
    }
}
