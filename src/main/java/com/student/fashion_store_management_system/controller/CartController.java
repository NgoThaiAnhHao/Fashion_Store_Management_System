package com.student.fashion_store_management_system.controller;
import com.student.fashion_store_management_system.model.entity.Cart;
import com.student.fashion_store_management_system.model.entity.CartItem;
import com.student.fashion_store_management_system.service.CartService;
import com.student.fashion_store_management_system.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fashion-store")
/**
 * TODO: Config for cart.html, method: addToCart
 */
public class CartController {
    private final ProductService productService;
    private final CartService cartService;

    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String showCart() {

        return "cart";
    }

    @PostMapping("/cart/add-to-cart/{productId}")
    public String addToCart(@ModelAttribute("cartItem") CartItem cartItem,
                            @PathVariable long productId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        cartItem.setProduct(productService.findById(productId));
        cartService.addToCart(session, cartItem);

        redirectAttributes.addFlashAttribute("successMessage", "Add to cart success!");
        return "redirect:/fashion-store/products/detail/" + productId;
    }

    @PostMapping("/cart/delete/{cartItemId}")
    public String deleteItem(@PathVariable int cartItemId,
                             HttpSession session) {
        cartService.deleteItem(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    @PostMapping("/cart/increase-pair-quantity/{cartItemId}")
    public String increasePairQuantity(@PathVariable int cartItemId,
                                   HttpSession session) {
        cartService.increasePairQuantity(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }

    @PostMapping("/cart/decrease-pair-quantity/{cartItemId}")
    public String decreasePairQuantity(@PathVariable int cartItemId,
                                       HttpSession session) {
        cartService.decreasePairQuantity(session, cartItemId);
        return "redirect:/fashion-store/cart";
    }
}
