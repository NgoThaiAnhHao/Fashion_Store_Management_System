package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.category.CategoryCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String findAll(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "/admin/category/category-management";
    }

    @GetMapping("/categories/add-new")
    public String showAddNewCategoryForm(Model model) {
        model.addAttribute("category", new CategoryCreateDto());
        return "/admin/category/add-new-category";
    }

    @PostMapping("/categories/add-new")
    public String addNewCategory(@Valid @ModelAttribute("category") CategoryCreateDto categoryCreateDto,
                                 BindingResult bindingResult,
                                 Model model) {
        // Check validation
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            return "/admin/category/add-new-category";
        }

        categoryService.addNew(categoryCreateDto);
        return "redirect:/fashion-store/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Deleted Success !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot deleted for this category because its have FK.");
        }

        return "redirect:/fashion-store/categories";
    }

    @GetMapping("/categories/update/{id}")
    public String showUpdateCategory(@PathVariable long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "/admin/category/edit-category";
    }

    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable long id,
                                 @Valid @ModelAttribute("category") CategoryCreateDto categoryCreateDto,
                                 BindingResult bindingResult,
                                 Model model) {
        // Check validation
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            Category category = categoryService.findById(id);
            model.addAttribute("category", category);
            model.addAttribute("errors", errors);
            return "/admin/category/edit-category";
        }

        categoryService.update(categoryCreateDto, id);
        return "redirect:/fashion-store/categories";
    }
}
