package com.student.fashion_store_management_system.repository;

import com.student.fashion_store_management_system.model.entity.Order;
import com.student.fashion_store_management_system.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
}
