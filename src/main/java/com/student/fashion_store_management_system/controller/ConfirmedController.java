package com.student.fashion_store_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fashion-store")
public class ConfirmedController {
    @GetMapping("/confirmed")
    public String confirmed() {
        return "confirmed";
    }
}
