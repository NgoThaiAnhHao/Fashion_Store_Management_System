package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import com.student.fashion_store_management_system.model.dto.authentication.UserUpdateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAll();

    List<UserResponseDto> findAllByRole(RoleNameEnum roleName);

    List<UserResponseDto> findAllByFullName(String keyword);

    UserResponseDto findById(long id);

    UserResponseDto updateProfile(UserUpdateDto userUpdateDto);

    void updateStatus(long id);

    void delete(long id) throws Exception;
}
