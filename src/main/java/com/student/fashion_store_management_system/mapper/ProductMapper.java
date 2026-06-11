package com.student.fashion_store_management_system.mapper;

import com.student.fashion_store_management_system.model.dto.product.ProductCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.model.entity.User;

public class ProductMapper {
    public static Product toEntity(ProductCreateDto productCreateDto, User currentUser, Category category) {
        return new Product(
                productCreateDto.getName(),
                productCreateDto.getDescription(),
                productCreateDto.getPrice(),
                productCreateDto.getImageUrl(),
                productCreateDto.getStockQuantity(),
                productCreateDto.getDiscountPercent(),
                category,
                currentUser
        );
    }

}
