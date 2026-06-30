package com.student.fashion_store_management_system.model.entity;

import com.student.fashion_store_management_system.enums.RoleNameEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private RoleNameEnum roleName;

    // Get separated role name. EX origin: ROLE_CUSTOMER, after get separated role name: CUSTOMER
    public String getSeparatedRoleName() {
        return roleName.toString().substring(5);
    }
}
