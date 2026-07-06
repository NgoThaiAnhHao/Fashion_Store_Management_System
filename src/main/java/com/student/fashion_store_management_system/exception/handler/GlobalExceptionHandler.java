package com.student.fashion_store_management_system.exception.handler;

import com.student.fashion_store_management_system.exception.common.DuplicateCategoryException;
import com.student.fashion_store_management_system.exception.common.DuplicateEmailException;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.dto.category.CategoryCreateDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "admin/category/add-new-category";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "page-not-found";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "page-not-found"; // Chuyển hướng đến trang lỗi chung
    }
}
