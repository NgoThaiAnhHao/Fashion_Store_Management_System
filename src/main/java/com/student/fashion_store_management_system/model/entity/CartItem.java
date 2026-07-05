package com.student.fashion_store_management_system.model.entity;

import com.student.fashion_store_management_system.enums.Gender; // Import new Gender enum
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

    private String member1Size; // Renamed
    private String member2Size; // Renamed

    private Gender member1Gender; // New field
    private Gender member2Gender; // New field

    private String customLogoText;
    private String customLogoImageUrl;

    // Removed @NotBlank annotations as requirement is conditional
    private String logoSize; // New field for logo size
    private String logoPosition; // New field for logo position

    private int member1Quantity = 1; // Renamed, default to 1
    private int member2Quantity = 1; // New field, default to 1

    private Product product;

    // Updated constructor
    public CartItem(String member1Size,
                    String member2Size,
                    Gender member1Gender,
                    Gender member2Gender,
                    String customLogoText,
                    String customLogoImageUrl,
                    String logoSize, // Add logoSize to constructor
                    String logoPosition, // Add logoPosition to constructor
                    int member1Quantity,
                    int member2Quantity,
                    Product product) {
        this.member1Size = member1Size;
        this.member2Size = member2Size;
        this.member1Gender = member1Gender;
        this.member2Gender = member2Gender;
        this.customLogoText = customLogoText;
        this.customLogoImageUrl = customLogoImageUrl;
        this.logoSize = logoSize; // Initialize logoSize
        this.logoPosition = logoPosition; // Initialize logoPosition
        this.member1Quantity = member1Quantity;
        this.member2Quantity = member2Quantity;
        this.product = product;
    }

    public BigDecimal getTotalOriginPriceByPairQuantity() {
        // Calculate total quantity for both members
        int totalQuantity = this.member1Quantity + this.member2Quantity;
        return product.getPrice().multiply(BigDecimal.valueOf(totalQuantity));
    }

    public BigDecimal getTotalSalePriceByPairQuantity() {
        // Calculate total quantity for both members
        int totalQuantity = this.member1Quantity + this.member2Quantity;
        return product.getSalePrice().multiply(BigDecimal.valueOf(totalQuantity));
    }

    // New methods for individual member quantities
    public BigDecimal getTotalOriginPriceMember1() {
        return product.getPrice().multiply(BigDecimal.valueOf(member1Quantity));
    }

    public BigDecimal getTotalSalePriceMember1() {
        return product.getSalePrice().multiply(BigDecimal.valueOf(member1Quantity));
    }

    public BigDecimal getTotalOriginPriceMember2() {
        return product.getPrice().multiply(BigDecimal.valueOf(member2Quantity));
    }

    public BigDecimal getTotalSalePriceMember2() {
        return product.getSalePrice().multiply(BigDecimal.valueOf(member2Quantity));
    }
}