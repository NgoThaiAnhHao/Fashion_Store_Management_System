package com.student.fashion_store_management_system.service;

import com.student.fashion_store_management_system.model.entity.User;
import com.student.fashion_store_management_system.repository.UserRepository;
import com.student.fashion_store_management_system.utils.CustomUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("CAN'T NOT FOUND USER")
                );

        return new CustomUserDetail(user);
    }
}
