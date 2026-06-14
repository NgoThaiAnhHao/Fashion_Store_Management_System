package com.student.fashion_store_management_system.repository;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import com.student.fashion_store_management_system.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT U FROM User U
        WHERE U.roles.roleName = :roleName
    """)
    List<User> findAllByRole(RoleNameEnum roleName);

    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
