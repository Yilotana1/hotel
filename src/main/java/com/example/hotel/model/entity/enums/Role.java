package com.example.hotel.model.entity.enums;

public enum Role {
    CLIENT("client"), MANAGER("manager");
    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
