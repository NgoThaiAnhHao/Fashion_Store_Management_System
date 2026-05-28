package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import com.student.fashion_store_management_system.exception.common.DuplicateEmailException;
import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.Role;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.RoleRepository;
import com.student.fashion_store_management_system.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void register(UserRegistrationDto userRegistrationDto) {
        // Check exist email
        if (isEmailExist(userRegistrationDto.getEmail())) {
            throw new DuplicateEmailException("EMAIL ALREADY EXIST, TRY AGAIN!");
        }

        // Mapper UserRegistrationDto -> User
        User user = UserMapper.toEntity(userRegistrationDto);

        // Get default role (ROLE_CUSTOMER)
        Role role = roleRepository
                .findByRoleName(RoleNameEnum.ROLE_CUSTOMER)
                .orElseThrow(
                    () -> new RuntimeException("ROLE NOT FOUND")
                 );

        // Set ROLE_CUSTOMER for user
        user.setRoles(role);

        // Save to database
        userRepository.save(user);
    }

    private boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
