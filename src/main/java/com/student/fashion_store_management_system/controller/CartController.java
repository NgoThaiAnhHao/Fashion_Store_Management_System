package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.enums.Gender; // Import Gender enum
import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.service.CartService;
import com.student.fashion_store_management_system.service.ProductService;
import com.student.fashion_store_management_system.utils.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Objects; // Import Objects for Objects.requireNonNull

@Controller
@RequestMapping("/fashion-store")
@AllArgsConstructor
public class CartController {

    private final ProductService productService;
    private final CartService cartService;

    @GetMapping("/cart")
    public String showCart() {
        return "cart";
    }

    @PostMapping("/cart/add-to-cart/{productId}")
    public String addToCart(@Valid @ModelAttribute("cartItem") CartItem cartItem,
                            BindingResult bindingResult,
                            @RequestParam(value = "customLogoImage", required = false) MultipartFile customLogoImage,
                            @RequestParam(value = "redirectToCart", required = false) String redirectToCart,
                            @PathVariable long productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) throws IOException {

        // Check validation errors (for other fields if any)
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/fashion-store/products/detail/" + productId;
        }

        // Business Rule Validation for logoSize and logoPosition
        boolean hasCustomText = cartItem.getCustomLogoText() != null && !cartItem.getCustomLogoText().trim().isEmpty();
        boolean hasCustomImage = customLogoImage != null && !customLogoImage.isEmpty();
        boolean hasCustomLogo = hasCustomText || hasCustomImage;

        if (hasCustomLogo) {
            if (cartItem.getLogoSize() == null || cartItem.getLogoSize().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Logo Size is required when custom text or logo image is provided.");
                return "redirect:/fashion-store/products/detail/" + productId;
            }
            if (cartItem.getLogoPosition() == null || cartItem.getLogoPosition().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Logo Position is required when custom text or logo image is provided.");
                return "redirect:/fashion-store/products/detail/" + productId;
            }
        }

        cartItem.setProduct(productService.findById(productId));

        if (customLogoImage != null && !customLogoImage.isEmpty()) {
            validateCustomLogo(customLogoImage);

            String originalFileName = StringUtils.cleanPath(
                    Objects.requireNonNull(customLogoImage.getOriginalFilename())
            );

            String extension = "";

            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                extension = originalFileName.substring(dotIndex);
            }

            String fileName = UUID.randomUUID() + extension;
            String uploadDir = "uploads/custom-logos";

            FileUploadUtil.saveFile(uploadDir, fileName, customLogoImage);

            cartItem.setCustomLogoImageUrl(fileName);
        }

        cartService.addToCart(session, cartItem);

        redirectAttributes.addFlashAttribute("successMessage", "Add to cart success!");

        if ("true".equals(redirectToCart)) {
            return "redirect:/fashion-store/cart";
        }

        return "redirect:/fashion-store/products/detail/" + productId;
    }

    @PostMapping("/cart/delete/{cartItemId}")
    public String deleteItem(@PathVariable int cartItemId,
                             HttpSession session) {
        cartService.deleteItem(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    // New methods for increasing/decreasing individual member quantities
    @PostMapping("/cart/increase-member1-quantity")
    public String increaseMember1Quantity(@RequestParam int cartItemId,
                                          HttpSession session) {
        cartService.increaseMember1Quantity(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    @PostMapping("/cart/decrease-member1-quantity")
    public String decreaseMember1Quantity(@RequestParam int cartItemId,
                                          HttpSession session) {
        cartService.decreaseMember1Quantity(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    @PostMapping("/cart/increase-member2-quantity")
    public String increaseMember2Quantity(@RequestParam int cartItemId,
                                          HttpSession session) {

        cartService.increaseMember2Quantity(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    @PostMapping("/cart/decrease-member2-quantity")
    public String decreaseMember2Quantity(@RequestParam int cartItemId,
                                          HttpSession session) {

        cartService.decreaseMember2Quantity(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    private void validateCustomLogo(MultipartFile file) {
        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("Logo image must be less than 5MB");
        }

        Set<String> allowedTypes = Set.of(
                "image/png",
                "image/jpeg",
                "image/jpg",
                "image/svg+xml",
                "image/webp"
        );

        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only PNG, JPG, SVG, WEBP files are allowed");
        }
    }
}