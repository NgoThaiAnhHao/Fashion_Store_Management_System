package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.dto.product.ProductCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.service.CategoryService;
import com.student.fashion_store_management_system.service.ProductService;
import com.student.fashion_store_management_system.utils.FileUploadUtil;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/fashion-store")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String findAll(Model model) {
        List<Product> products = productService.findAll();
        products.forEach(System.out::println);
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
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
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

        Product savedProduct = productService.addNew(productCreateDto);

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
                                Model model) {
        productService.delete(id);
        return "redirect:/fashion-store/products";
    }

    @GetMapping("/products/update/{id}")
    public String showUpdateProduct(@PathVariable long id,
                              Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", findAllCategories());
        return "/admin/product/edit-product";
    }

    @PostMapping("/products/update/{id}")
    public String updateProduct(@PathVariable long id,
                                @Valid @ModelAttribute("product") ProductCreateDto productCreateDto,
                                BindingResult bindingResult,
                                Model model) {
        // Check validation
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            model.addAttribute("errors", errors);
            model.addAttribute("categories", findAllCategories());
            model.addAttribute("product", productCreateDto);
            return "/admin/product/add-new-product";
        }

        productService.update(productCreateDto, id);
        return "redirect:/fashion-store/products";
    }

    @GetMapping("/products/detail/{id}")
    public String showDetailForm(@PathVariable long id,
                                 Model model) {
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
}
