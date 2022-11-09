package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.Role;

import java.util.Collection;

public class RoleAssignment {
    private Long id;
    private Long userId;
    private final Collection<Role> roles;

    public RoleAssignment(Long id, Long userId, Collection<Role> roles) {
        this.id = id;
        this.userId = userId;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Collection<Role> getRoles() {
        return roles;
    }
}
