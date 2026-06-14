package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.entity.Cart;
import com.student.fashion_store_management_system.model.entity.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    public void addToCart(HttpSession session, CartItem cartItem) {
        Cart cart = (Cart) session.getAttribute("cart");
        if(cart == null){
            cart = new Cart();
        }

        cart.addItem(cartItem);
        session.setAttribute("cart", cart);
    }

    public void deleteItem(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");

        cart.deleteItem(cartItemId);
        session.setAttribute("cart", cart);
    }

    public void increasePairQuantity(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");

        cart.increasePairQuantity(cartItemId);
        session.setAttribute("cart", cart);
    }

    public void decreasePairQuantity(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");

        cart.decreasePairQuantity(cartItemId);
        session.setAttribute("cart", cart);
    }
}
