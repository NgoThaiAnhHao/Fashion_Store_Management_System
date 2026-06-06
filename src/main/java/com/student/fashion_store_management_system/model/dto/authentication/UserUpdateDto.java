package com.student.fashion_store_management_system.model.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {
    private long userId;

    private String avatarUrl;

    @NotBlank(message = "* Your full name is mandatory!")
    @Size(min = 3, max = 30, message = "* Your full name must be at least 3 characters long!")
    private String fullName;

    private String phone;

    private String homeAddress;

    public UserUpdateDto() {
    }

    public UserUpdateDto(String avatarUrl, String fullName, String phone, String homeAddress) {
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.phone = phone;
        this.homeAddress = homeAddress;
    }

    public UserUpdateDto(long userId, String avatarUrl, String fullName, String phone, String homeAddress) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.fullName = fullName;
        this.phone = phone;
        this.homeAddress = homeAddress;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "UserUpdateDto{" +
                "userId=" + userId +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                '}';
    }
}
