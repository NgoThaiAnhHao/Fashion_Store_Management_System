package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import com.student.fashion_store_management_system.exception.common.DuplicateEmailException;
import com.student.fashion_store_management_system.exception.common.ResourceNotFoundException;
import com.student.fashion_store_management_system.mapper.UserMapper;
import com.student.fashion_store_management_system.model.dto.authentication.UserRegistrationDto;
import com.student.fashion_store_management_system.model.entity.Role;
import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.RoleRepository;
import com.student.fashion_store_management_system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
                    () -> new ResourceNotFoundException("ROLE NOT FOUND")
                 );

        // Set ROLE_CUSTOMER for user
        user.setRoles(role);

        // Encode password
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        // Save to database
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        // Get current user who logged in
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Return null if user do not logged in
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        // Get current email (user who logged in)
        String username = authentication.getName();

        // Return current User
        return userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("USER NOT FOUND")
                );
    }

    private boolean isEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
