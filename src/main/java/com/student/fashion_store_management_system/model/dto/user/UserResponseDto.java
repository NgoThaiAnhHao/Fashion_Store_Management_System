package com.student.fashion_store_management_system.model.dto.user;

import com.student.fashion_store_management_system.model.entity.Role;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserResponseDto {
    private long userId;
    private String email;
    private String fullName;
    private String phone;
    private String homeAddress;
    private String avatarUrl;
    private boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;

    public UserResponseDto() {
    }

    public UserResponseDto(long userId, String email, String fullName, String phone, String homeAddress, String avatarUrl, boolean isEnabled, LocalDateTime createdAt, LocalDateTime updatedAt, Role role) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.homeAddress = homeAddress;
        this.avatarUrl = avatarUrl;
        this.isEnabled = isEnabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstCharOfFullName() {
        return this.fullName.substring(0, 1);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
        );
    }

    public String getFormattedUpdatedAt() {
        return updatedAt.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")
        );
    }
}
