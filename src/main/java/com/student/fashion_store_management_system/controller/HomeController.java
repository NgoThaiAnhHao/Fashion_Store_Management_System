package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/home")
public class HomeController {

    private final AuthenticationService authenticationService;

    public HomeController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String home() {
        // Log current user for debug
        User user = authenticationService.getCurrentUser();
        if (user != null) {
            System.out.println("CURRENT USER EMAIL: " + user.getEmail());
        } else {
            System.out.println("CURRENT USER EMAIL: Anonymous");
        }
        return "home";
    }
}
