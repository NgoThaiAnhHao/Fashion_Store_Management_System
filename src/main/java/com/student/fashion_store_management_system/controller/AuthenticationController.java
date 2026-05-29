package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/v1/auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // GET: REGISTER
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    // POST: REGISTER
    @PostMapping("/register")
    public String userRegistration(@Valid @ModelAttribute("user") UserRegistrationDto userRegistrationDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        System.out.println("BINDING: " + bindingResult.hasErrors());
        // Check if invalidation input
        if (bindingResult.hasErrors()) {
            return "register";
        }

        authenticationService.register(userRegistrationDto);
        redirectAttributes.addFlashAttribute("successMessage", "Create account successfully, try to login now!");
        return "redirect:/v1/auth/login";
    }

    // GET: LOGIN
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
