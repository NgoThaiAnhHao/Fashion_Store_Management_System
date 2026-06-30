package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.entity.Cart;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fashion-store")
public class ConfirmedController {

    @GetMapping("/confirmed")
    public String confirmed(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            cart.clear();
        }

        return "confirmed";
    }

}
