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
        if (cart != null) {
            cart.deleteItem(cartItemId);
            session.setAttribute("cart", cart);
        }
    }

    // New methods for increasing/decreasing individual member quantities
    public void increaseMember1Quantity(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.increaseMember1Quantity(cartItemId);
            session.setAttribute("cart", cart);
        }
    }

    public void decreaseMember1Quantity(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.decreaseMember1Quantity(cartItemId);
            session.setAttribute("cart", cart);
        }
    }

    public void increaseMember2Quantity(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.increaseMember2Quantity(cartItemId);
            session.setAttribute("cart", cart);
        }
    }

    public void decreaseMember2Quantity(HttpSession session, int cartItemId) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            cart.decreaseMember2Quantity(cartItemId);
            session.setAttribute("cart", cart);
        }
    }
}
