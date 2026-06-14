package com.student.fashion_store_management_system.repository;

import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderedBy(User user);

    List<Order> findByOrderedBy_FullNameContainingIgnoreCase(String fullName);

}
