package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.Gender; // Import Gender enum
import com.student.fashion_store_management_system.exception.common.ProductUpdateRestrictedException;
import com.student.fashion_store_management_system.model.dto.product.ProductCreateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.service.CategoryService;
import com.student.fashion_store_management_system.service.ProductService;
import com.student.fashion_store_management_system.utils.FileUploadUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/products")
    public String findAll(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/admin/product/product-management";
    }

    @GetMapping("/products/search-by-name")
    public String searchByName(@RequestParam String keyword, Model model) {
        List<Product> products = productService.findByProductName(keyword);
        model.addAttribute("products", products);
        return "/admin/product/product-management";
    }

    @GetMapping("/products/add-new")
    public String showAddNew(Model model) {
        model.addAttribute("product", new ProductCreateDto());
        model.addAttribute("categories", findAllCategories());
        return "/admin/product/add-new-product";
    }

    @PostMapping("/products/add-new")
    public String addNew(@Valid @ModelAttribute("product") ProductCreateDto productCreateDto,
                         BindingResult bindingResult,
                         @RequestParam("image") MultipartFile multipartFile,
                         Model model) {
        // Check validation
        if (bindingResult.hasErrors()) {
            List<String> errors = getValidationMessages(bindingResult.getFieldErrors());
            log.warn("Product creation rejected. name='{}', price={}, validationErrors={}",
                    productCreateDto.getName(), productCreateDto.getPrice(), errors);
            model.addAttribute("errors", errors);
            addPriceError(bindingResult, model);
            model.addAttribute("categories", findAllCategories());
            model.addAttribute("product", productCreateDto);
            return "/admin/product/add-new-product";
        }

        // Processing for upload image
        String fileName = "";
        if (multipartFile != null && !multipartFile.isEmpty()) {

            // Get file name: ex: product.jpg
            fileName = StringUtils.cleanPath(
                    Objects.requireNonNull(multipartFile.getOriginalFilename())
            );

            // Set for product
            productCreateDto.setImageUrl(fileName);
        }

        Product savedProduct;
        try {
            savedProduct = productService.addNew(productCreateDto);
        } catch (DataAccessException e) {
            log.error("Failed to create product due to invalid database value: name='{}', price={}",
                    productCreateDto.getName(), productCreateDto.getPrice(), e);
            model.addAttribute("errors", List.of(
                    "Product could not be saved. Price must not exceed 99,999,999.99."
            ));
            model.addAttribute("categories", findAllCategories());
            model.addAttribute("product", productCreateDto);
            return "/admin/product/add-new-product";
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            // Create folder for every product
            String uploadDir = "uploads/products/" + savedProduct.getProductId();

            // Save avatar's data
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return "redirect:/fashion-store/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable long id,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Deleted Success!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cannot deleted for this product because its have FK for Order.");
        }

        return "redirect:/fashion-store/products";
    }

    @GetMapping("/products/update/{id}")
    public String showUpdateProduct(@PathVariable long id,
                              Model model) {
        Product productEntity = productService.findById(id);

        // Convert Product entity to ProductCreateDto for form binding
        ProductCreateDto productCreateDto = new ProductCreateDto();
        productCreateDto.setName(productEntity.getName());
        productCreateDto.setDescription(productEntity.getDescription());
        productCreateDto.setPrice(productEntity.getPrice());
        productCreateDto.setStockQuantity(productEntity.getStockQuantity());
        productCreateDto.setDiscountPercent(productEntity.getDiscountPercent());
        productCreateDto.setImageUrl(productEntity.getImageUrl());
        if (productEntity.getCategory() != null) {
            productCreateDto.setCategoryId(productEntity.getCategory().getCategoryId());
        }

        model.addAttribute("product", productCreateDto); // 'product' is now a ProductCreateDto
        model.addAttribute("productId", id); // Add productId separately
        model.addAttribute("categories", findAllCategories());
        return "/admin/product/edit-product";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable long id,
                                @Valid @ModelAttribute("product") ProductCreateDto productCreateDto,
                                BindingResult bindingResult,
                                Model model) {
        // Always add productId to the model for the form action URL
        model.addAttribute("productId", id);

        // Check validation
        if (bindingResult.hasErrors()) {
            List<String> errors = getValidationMessages(bindingResult.getFieldErrors());
            log.warn("Product update rejected. productId={}, name='{}', price={}, validationErrors={}",
                    id, productCreateDto.getName(), productCreateDto.getPrice(), errors);
            model.addAttribute("errors", errors);
            addPriceError(bindingResult, model);
            model.addAttribute("categories", findAllCategories());
            // Re-add productCreateDto to retain user input
            model.addAttribute("product", productCreateDto);
            return "/admin/product/edit-product"; // Return to edit page
        }

        try {
            productService.update(productCreateDto, id);
        } catch (ProductUpdateRestrictedException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", findAllCategories());
            // Re-add productCreateDto to retain user input
            model.addAttribute("product", productCreateDto);
            return "/admin/product/edit-product"; // Return to edit page
        }
        return "redirect:/fashion-store/products";
    }

    @GetMapping("/products/detail/{id}")
    public String showDetailForm(@PathVariable long id,
                                 Model model) {
        // Initialize CartItem with default values for new fields
        CartItem cartItem = new CartItem(
                "L", "L", // Default sizes
                Gender.MALE, Gender.FEMALE, // Default genders
                null, null, // No custom text/image by default
                null, null, // Default logoSize and logoPosition
                1, 1, // Default quantities
                productService.findById(id) // Product
        );
        model.addAttribute("cartItem", cartItem);
        model.addAttribute("product", productService.findById(id));
        return "product-detail";
    }

    private List<Category> findAllCategories() {
        List<Category> categories = categoryService.findAll();

        // Throw exception for user input the categories first time
        if (categories == null || categories.isEmpty()) {
            return null;
        }

        return categories;
    }

    private List<String> getValidationMessages(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(this::getValidationMessage)
                .toList();
    }

    private void addPriceError(BindingResult bindingResult, Model model) {
        FieldError priceError = bindingResult.getFieldError("price");
        if (priceError != null) {
            model.addAttribute("priceError", getValidationMessage(priceError));
        }
    }

    private String getValidationMessage(FieldError error) {
        if ("categoryId".equals(error.getField()) && "typeMismatch".equals(error.getCode())) {
            return "Please select a valid category.";
        }
        if ("price".equals(error.getField()) && "typeMismatch".equals(error.getCode())) {
            return "Price must be a valid number from 0 to 99,999,999.99. Use a dot for decimals and no more than 2 decimal places (for example: 80.00).";
        }
        return error.getDefaultMessage();
    }
}
