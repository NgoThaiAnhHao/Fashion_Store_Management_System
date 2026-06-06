package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.dto.authentication.UserUpdateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.User;

public interface UserService {
    UserResponseDto findById(long id);

    UserResponseDto updateProfile(UserUpdateDto userUpdateDto);
}
