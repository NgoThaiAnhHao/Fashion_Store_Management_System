package com.student.fashion_store_management_system.repository;

import com.student.fashion_store_management_system.model.entity.Product;
import com.student.fashion_store_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
}
