package com.student.fashion_store_management_system.config;

import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.service.UserService;
import com.student.fashion_store_management_system.utils.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addCurrentUser(Model model, Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            CustomUserDetail principal = (CustomUserDetail) authentication.getPrincipal();

            model.addAttribute("currentUser", UserMapper.toResponse(principal.getUser()));
        }
    }
}
