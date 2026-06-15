package com.student.fashion_store_management_system.config;

import com.student.fashion_store_management_system.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customUserDetailService = customUserDetailService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Config path
        httpSecurity
                .authorizeHttpRequests(config ->
                    config
                        .requestMatchers(
                                "/fashion-store/auth/**",
                                "/fashion-store/dashboard",
                                "/css/**",
                                "/images/**",
                                "/fashion-store/dashboard/test",
                                "/fashion-store/products/detail/**",
                                "/fashion-store/products",
                                "/uploads/products/**"
                        ).permitAll()
                        .requestMatchers(
                                "/fashion-store/checkout",
                                "/fashion-store/payment").hasRole("CUSTOMER")
                        .requestMatchers("/fashion-store/users/users-management/**").hasRole("ADMIN")
                        .requestMatchers(
                                "/fashion-store/categories/**",
                                "/fashion-store/orders/**",
                                "/fashion-store/payment/**",
                                "/fashion-store/products/**").hasAnyRole("ADMIN", "MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/fashion-store/auth/login")
                        .loginProcessingUrl("/fashion-store/auth/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/fashion-store/auth/logout")
                        .logoutSuccessUrl("/fashion-store/auth/login?logout")
                        .permitAll()
                )
                .exceptionHandling(config -> config
                        .accessDeniedPage("/fashion-store/denied-page")
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(customUserDetailService);

        // Say with Spring Security is use Bcrypt to comparison
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
}
