package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.exception.common.DuplicateEmailException;
import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(UserRegistrationDto userRegistrationDto) {
        // Check exist email
        if (isEmailExist(userRegistrationDto.getEmail())) {
            throw new DuplicateEmailException("EMAIL ALREADY EXIST, TRY AGAIN!");
        }

        // Mapper UserRegistrationDto -> User
        User user = UserMapper.toEntity(userRegistrationDto);

        // Save to database
        userRepository.save(user);
    }

    private boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
