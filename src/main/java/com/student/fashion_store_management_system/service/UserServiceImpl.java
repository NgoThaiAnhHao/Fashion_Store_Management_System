package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.model.dto.authentication.UserUpdateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto findById(long id) {
        return UserMapper.toResponse(
                userRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("USER NOT FOUND"))
        );
    }

    @Override
    public UserResponseDto updateProfile(UserUpdateDto userUpdateDto) {
        User user = userRepository
                .findById(userUpdateDto.getUserId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("USER NOT FOUND"));

        user.setPhone(userUpdateDto.getPhone());
        user.setFullName(userUpdateDto.getFullName());
        user.setAvatarUrl(userUpdateDto.getAvatarUrl());
        user.setHomeAddress(userUpdateDto.getHomeAddress());

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

}
