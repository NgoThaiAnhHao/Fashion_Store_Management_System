package com.student.fashion_store_management_system.model.entity;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleNameEnum roleName;

    public Role() {
    }

    public Role(int roleId, RoleNameEnum roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleNameEnum getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleNameEnum roleName) {
        this.roleName = roleName;
    }
}
