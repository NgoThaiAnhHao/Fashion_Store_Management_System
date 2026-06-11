package com.student.fashion_store_management_system.exception.handler;

import com.student.fashion_store_management_system.exception.common.DuplicateCategoryException;
import com.student.fashion_store_management_system.exception.common.DuplicateEmailException;
import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.dto.category.CategoryCreateDto;
import com.student.fashion_store_management_system.model.entity.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEmailException.class)
    public String handleDuplicateEmail(DuplicateEmailException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public String handleDuplicateCategory(DuplicateCategoryException e, Model model) {
        model.addAttribute("errors", List.of(e.getMessage()));
        model.addAttribute("category", new CategoryCreateDto());
        return "/admin/add-new-category";
    }
}
