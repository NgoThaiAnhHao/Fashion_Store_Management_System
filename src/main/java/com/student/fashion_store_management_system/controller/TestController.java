package com.student.fashion_store_management_system.controller;

import com.student.fashion_store_management_system.model.entity.Role;
import com.student.fashion_store_management_system.repository.RoleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class TestController {

    private final RoleRepository roleRepository;

    public TestController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String testController() {
        return "register";
    }
}
