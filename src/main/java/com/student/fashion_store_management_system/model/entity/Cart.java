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
                                        && Objects.equals(item.getMember1Size(), cartItem.getMember1Size()) // Updated field name
                                        && Objects.equals(item.getMember2Size(), cartItem.getMember2Size()) // Updated field name
                                        && Objects.equals(item.getMember1Gender(), cartItem.getMember1Gender()) // New field
                                        && Objects.equals(item.getMember2Gender(), cartItem.getMember2Gender()) // New field
                                        && Objects.equals(normalize(item.getCustomLogoText()), normalize(cartItem.getCustomLogoText()))
                                        && Objects.equals(normalize(item.getCustomLogoImageUrl()), normalize(cartItem.getCustomLogoImageUrl()))
                        )
                        .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setMember1Quantity(item.getMember1Quantity() + cartItem.getMember1Quantity()); // Updated quantity field
            item.setMember2Quantity(item.getMember2Quantity() + cartItem.getMember2Quantity()); // New quantity field
            return;
        }

        cartItem.setCartItemId(nextId++);
        items.add(cartItem);
    }

    public void deleteItem(int cartItemId) {
        items.removeIf(item -> item.getCartItemId() == cartItemId);
    }

    // New methods for increasing/decreasing individual member quantities
    public void increaseMember1Quantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getMember1Quantity() < 100) { // Max quantity limit
                        item.setMember1Quantity(item.getMember1Quantity() + 1);
                    }
                });
    }

    public void decreaseMember1Quantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getMember1Quantity() > 0) { // Min quantity limit
                        item.setMember1Quantity(item.getMember1Quantity() - 1);
                    }
                });
    }

    public void increaseMember2Quantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getMember2Quantity() < 100) { // Max quantity limit
                        item.setMember2Quantity(item.getMember2Quantity() + 1);
                    }
                });
    }

    public void decreaseMember2Quantity(int cartItemId) {
        items.stream()
                .filter(item -> item.getCartItemId() == cartItemId)
                .findFirst()
                .ifPresent(item -> {
                    if (item.getMember2Quantity() > 0) { // Min quantity limit
                        item.setMember2Quantity(item.getMember2Quantity() - 1);
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
