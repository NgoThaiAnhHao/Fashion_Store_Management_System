package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.mapper.ProductMapper;
import com.student.fashion_store_management_system.model.dto.product.ProductCreateDto;
import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AuthenticationService authenticationService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, AuthenticationService authenticationService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByProductName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Product findById(long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("PRODUCT NOT FOUND")
                );
    }

    @Override
    @Transactional
    public Product addNew(ProductCreateDto productCreateDto) {
        User currentUser = authenticationService.getCurrentUser();
        Category category = categoryService.findById(productCreateDto.getCategoryId());
        return productRepository.save(
                ProductMapper.toEntity(productCreateDto, currentUser, category)
        );
    }

    @Override
    @Transactional
    public Product update(ProductCreateDto productCreateDto, long id) {
        Product product = findById(id);
        Category category = categoryService.findById(productCreateDto.getCategoryId());

        // Update fields
        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setPrice(productCreateDto.getPrice());
        product.setStockQuantity(productCreateDto.getStockQuantity());
        product.setDiscountPercent(productCreateDto.getDiscountPercent());
        product.setCategory(category);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void delete(long id) {
        productRepository
                .deleteById(id);
    }

    @Override
    @Transactional
    public void degreeStockQuantity(long id, int quantity) {
        Product product = findById(id);
        product.setStockQuantity(
                product.getStockQuantity() - quantity
        );

        productRepository.save(product);
    }
}
