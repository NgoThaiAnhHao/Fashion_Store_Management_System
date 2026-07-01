package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CustomController {

    private final ProductService productService;

    @GetMapping("/fashion-store/custom")
    public String showCustomPage(Model model) {
        List<Product> products = productService.findAll();

        model.addAttribute("products", products);
        model.addAttribute("cartItem", new CartItem());

        return "custom";
    }
}