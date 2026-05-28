package com.student.fashion_store_management_system.model.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    @NotBlank(message = "* Your full name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your full name must be at least 3 characters long!")
    private String fullName;

    @NotBlank(message = "* Email is mandatory!")
    @Email(message = "* Invalid email format!")
    private String email;

    @NotBlank(message = "* Password is mandatory!")
    @Size(min = 5, max = 30, message = "* Password must be at least 5 characters long!")
    private String password;

    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
