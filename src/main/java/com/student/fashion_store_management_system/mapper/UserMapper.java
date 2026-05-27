package com.student.fashion_store_management_system.mapper;

import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.User;

public class UserMapper {
    public static User toEntity(UserRegistrationDto userRegistrationDto) {
        return new User(
                userRegistrationDto.getEmail(),
                userRegistrationDto.getPassword(),
                userRegistrationDto.getFullName()
        );
    }
}
