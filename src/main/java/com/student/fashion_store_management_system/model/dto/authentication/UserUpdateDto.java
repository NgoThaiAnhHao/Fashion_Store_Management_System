package com.student.fashion_store_management_system.model.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private long userId;

    private String avatarUrl;

    @NotBlank(message = "* Your full name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your full name must be at least 3 characters long!")
    private String fullName;

    private String phone;

    private String homeAddress;


    public UserUpdateDto(String avatarUrl, String fullName, String phone, String homeAddress) {
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.phone = phone;
        this.homeAddress = homeAddress;
    }
}
