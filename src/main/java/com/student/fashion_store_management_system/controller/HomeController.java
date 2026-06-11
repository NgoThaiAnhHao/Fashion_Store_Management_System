package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import com.student.fashion_store_management_system.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * TODO: FIX PROFILE PHOTO,
 * BUG Avatar at HOME>layout.html
 * Check user.avatar
 */
@Controller
@RequestMapping("/fashion-store/dashboard")
public class HomeController {

    private final ProductService productService;
    private final AuthenticationService authenticationService;

    public HomeController(ProductService productService, AuthenticationService authenticationService) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String home(Model model) {
        // Log current user for debug
        User user = authenticationService.getCurrentUser();

        if (user != null) {
            System.out.println("CURRENT USER EMAIL: " + user.getEmail());
            System.out.println("USER INFO: " + user.toString());
            System.out.println("USER IMG: " + user.getAvatarUrl());
        } else {
            System.out.println("CURRENT USER EMAIL: Anonymous");
        }

        if (user != null && "ROLE_ADMIN".equals(user.getRoles().getRoleName().toString())) {
            return "/admin/admin-dashboard";
        }

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "dashboard";
    }

    @GetMapping("/test")
    public String test() {
        return "/admin/category/add-new-category";
    }
}
