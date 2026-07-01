package com.student.fashion_store_management_system.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private int cartItemId;

    private String maleSize;

    private String femaleSize;

    private String customLogoText;

    private String customLogoImageUrl;

    private int pairQuantity = 1;

    private Product product;

    public CartItem(String maleSize,
                    String femaleSize,
                    String customLogoText,
                    String customLogoImageUrl,
                    int pairQuantity,
                    Product product) {
        this.maleSize = maleSize;
        this.femaleSize = femaleSize;
        this.customLogoText = customLogoText;
        this.customLogoImageUrl = customLogoImageUrl;
        this.pairQuantity = pairQuantity;
        this.product = product;
    }

    public BigDecimal getTotalOriginPriceByPairQuantity() {
        return product.getPrice().multiply(BigDecimal.valueOf(pairQuantity));
    }

    public BigDecimal getTotalSalePriceByPairQuantity() {
        return product.getSalePrice().multiply(BigDecimal.valueOf(pairQuantity));
    }
}