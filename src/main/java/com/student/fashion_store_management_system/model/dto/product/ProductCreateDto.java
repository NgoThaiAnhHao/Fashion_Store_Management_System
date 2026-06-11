package com.student.fashion_store_management_system.model.dto.product;

import com.student.fashion_store_management_system.model.entity.Category;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

public class ProductCreateDto {

    @NotBlank(message = "* Product name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your product name must be at least 3 characters long!")
    private String name;

    private String description;

    @NotNull(message = "* Price must be not null")
    @Min(value = 0, message = "* Price must be greater than 0")
    private BigDecimal price;

    private String imageUrl;

    @NotNull(message = "* Stock quantity must be not null")
    @Min(value = 0, message = "* Stock quantity be greater than 0")
    @Max(value = 999, message = "* Stock quantity be least than 999")
    private Integer stockQuantity;

    @Min(value = 0, message = "* Discount percent be greater than 0")
    @Max(value = 100, message = "* Discount percent be least than 100")
    private Integer discountPercent;

    @NotNull(message = "* Category is mandatory!")
    private long categoryId;

    public ProductCreateDto() {
    }

    public ProductCreateDto(String name, String description, BigDecimal price, String imageUrl, Integer  stockQuantity, Integer  discountPercent, long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.discountPercent = discountPercent;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer  getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer  getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ProductCreateDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", discountPercent=" + discountPercent +
                ", categoryId=" + categoryId +
                '}';
    }
}
