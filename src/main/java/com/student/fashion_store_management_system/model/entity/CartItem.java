package com.student.fashion_store_management_system.model.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItem {

    private int cartItemId;

    private String maleSize;

    private String femaleSize;

    private String customLogoText;

    private int pairQuantity = 1;

    private Product product;

    public CartItem() {
    }

    public CartItem(String maleSize, String femaleSize, String customLogoText, int pairQuantity, Product product) {
        this.maleSize = maleSize;
        this.femaleSize = femaleSize;
        this.customLogoText = customLogoText;
        this.pairQuantity = pairQuantity;
        this.product = product;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getMaleSize() {
        return maleSize;
    }

    public void setMaleSize(String maleSize) {
        this.maleSize = maleSize;
    }

    public String getFemaleSize() {
        return femaleSize;
    }

    public void setFemaleSize(String femaleSize) {
        this.femaleSize = femaleSize;
    }

    public String getCustomLogoText() {
        return customLogoText;
    }

    public void setCustomLogoText(String customLogoText) {
        this.customLogoText = customLogoText;
    }

    public int getPairQuantity() {
        return pairQuantity;
    }

    public void setPairQuantity(int pairQuantity) {
        this.pairQuantity = pairQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getTotalOriginPriceByPairQuantity() {
        return product.getPrice().multiply(
                BigDecimal.valueOf(pairQuantity)
        );
    }

    public BigDecimal getTotalSalePriceByPairQuantity() {
        return product.getSalePrice().multiply(
                BigDecimal.valueOf(pairQuantity)
        );
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "maleSize='" + maleSize + '\'' +
                ", femaleSize='" + femaleSize + '\'' +
                ", customLogoText='" + customLogoText + '\'' +
                ", pairQuantity=" + pairQuantity +
                ", product=" + product +
                '}';
    }
}
