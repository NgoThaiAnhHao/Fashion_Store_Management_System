package com.student.fashion_store_management_system.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;

    @Column(name = "name", nullable = false)
    private String name;

    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    private String imageUrl;

    private int stockQuantity;

    private int discountPercent;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    public Product(String name, String description, BigDecimal price, String imageUrl, int stockQuantity, int discountPercent, Category category, User createdBy) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.discountPercent = discountPercent;
        this.category = category;
        this.createdBy = createdBy;
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(
                DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm")
        );
    }

    public String getFormattedUpdatedAt() {
        return updatedAt.format(
                DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm")
        );
    }

    public BigDecimal getSalePrice() {
        if (discountPercent <= 0) {
            return price;
        }

        return price
                .multiply(BigDecimal.valueOf(100 - discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", discountPercent=" + discountPercent +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", category=" + category +
                '}';
    }
}
