package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.User;

public interface AuthenticationService {
    void register(UserRegistrationDto userRegistrationDto);
}
