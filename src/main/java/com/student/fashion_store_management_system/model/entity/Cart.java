package com.student.fashion_store_management_system.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Setter
@Getter
public class Cart {
    private int nextId = 1;

    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem cartItem) {
        Optional<CartItem> existingItem =
                items.stream()
                        .filter(item ->
                                item.getProduct().getProductId() == cartItem.getProduct().getProductId()
                                        && Objects.equals(item.getMaleSize(), cartItem.getMaleSize())
                                        && Objects.equals(item.getFemaleSize(), cartItem.getFemaleSize())
                                        && Objects.equals(normalize(item.getCustomLogoText()), normalize(cartItem.getCustomLogoText()))
                                        && Objects.equals(normalize(item.getCustomLogoImageUrl()), normalize(cartItem.getCustomLogoImageUrl()))
                        )
                        .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setPairQuantity(item.getPairQuantity() + cartItem.getPairQuantity());
            return;
        }

        cartItem.setCartItemId(nextId++);
        items.add(cartItem);
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
                        item.setPairQuantity(item.getPairQuantity() + 1);
                    }
                });
    }

    public void decreasePairQuantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getPairQuantity() > 1) {
                        item.setPairQuantity(item.getPairQuantity() - 1);
                    }
                });
    }

    public BigDecimal getTotalOriginAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            total = total.add(item.getTotalOriginPriceByPairQuantity());
        }

        return total;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : items) {
            total = total.add(item.getTotalSalePriceByPairQuantity());
        }

        return total;
    }

    public void clear() {
        items.clear();
        nextId = 1;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}