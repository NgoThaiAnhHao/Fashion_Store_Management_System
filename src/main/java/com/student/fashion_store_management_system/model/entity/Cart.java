package com.student.fashion_store_management_system.model.entity;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private int nextId = 1;
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem cartItem) {
        // Find existing cart item
        Optional<CartItem> existingItem =
                items.stream()
                        .filter(item ->
                                item.getProduct().getProductId() == cartItem.getProduct().getProductId()
                                        && item.getMaleSize().equals(cartItem.getMaleSize())
                                        && item.getFemaleSize().equals(cartItem.getFemaleSize())
                        )
                        .findFirst();

        // If existing cart item, plus the pair quantity
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setPairQuantity(
                    item.getPairQuantity()
                            + cartItem.getPairQuantity()
            );
            return;
        }

        cartItem.setCartItemId(nextId++);
        items.add(cartItem);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void deleteItem(int cartItemId) {
        items.removeIf(item -> item.getCartItemId() == cartItemId);
    }

    public void increasePairQuantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                        if (item.getPairQuantity() <= 100) {
                            item.setPairQuantity(
                                    item.getPairQuantity() + 1
                            );                        }
                    }
                );
    }

    public void decreasePairQuantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                            if (item.getPairQuantity() > 1) {
                                item.setPairQuantity(
                                        item.getPairQuantity() - 1
                                );
                            }
                        }
                );
    }

    public BigDecimal getTotalOriginAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            total = total.add(
                    item.getTotalOriginPriceByPairQuantity()
            );
        }

        return total;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            total = total.add(
                    item.getTotalSalePriceByPairQuantity()
            );
        }

        return total;
    }
}
