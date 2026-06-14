package com.student.fashion_store_management_system.repository;

import com.student.fashion_store_management_system.model.entity.Category;
import com.student.fashion_store_management_system.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByNameContainingIgnoreCase(String name);
}
