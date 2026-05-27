package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1/auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    /**
     * TODO: Custom for error message (Validation) in html
     * @param userRegistrationDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/register")
    public String userRegistration(@Valid @ModelAttribute("user") UserRegistrationDto userRegistrationDto,
                                   BindingResult bindingResult) {
        System.out.println("BINDING: " + bindingResult.hasErrors());
        // Check if invalidation input
        if (bindingResult.hasErrors()) {
            return "register";
        }

        authenticationService.register(userRegistrationDto);
        return "login";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}
