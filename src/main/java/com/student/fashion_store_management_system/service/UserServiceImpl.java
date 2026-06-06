package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.model.dto.authentication.UserUpdateDto;
import com.student.fashion_store_management_system.model.dto.user.UserResponseDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
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

    @Override
    public void updateStatus(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("USER NOT FOUND"));

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("USER NOT FOUND"));

        // No delete current user
        if (user.getUserId() == authenticationService.getCurrentUser().getUserId()) {
            return;
        }

        // No delete user who is admin
        if ("ROLE_ADMIN".equals(user.getRoles().getRoleName().toString())) {
            return;
        }

        userRepository.deleteById(id);
    }

}
