package com.student.fashion_store_management_system.repository;

import com.student.fashion_store_management_system.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
}
