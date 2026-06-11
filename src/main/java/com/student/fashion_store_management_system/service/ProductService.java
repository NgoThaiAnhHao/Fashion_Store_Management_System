package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.dto.product.ProductCreateDto;
import com.student.fashion_store_management_system.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    Product findById(long id);

    Product addNew(ProductCreateDto productCreateDto);

    Product update(ProductCreateDto productCreateDto, long id);

    void delete(long id);
}
