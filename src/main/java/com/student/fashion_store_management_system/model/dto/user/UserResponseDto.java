package com.student.fashion_store_management_system.model.dto.user;

import com.student.fashion_store_management_system.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public String getFirstCharOfFullName() {
        return this.fullName.substring(0, 1);
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
