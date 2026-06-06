package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.User;

import java.util.Optional;

public interface AuthenticationService {
    void register(UserRegistrationDto userRegistrationDto);

    User getCurrentUser();
}
