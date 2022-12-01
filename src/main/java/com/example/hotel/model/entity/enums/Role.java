package com.example.hotel.model.entity.enums;

public enum Role {
    CLIENT("client"), MANAGER("manager"), ADMIN("admin");
    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role getFromString(String role) {
        switch (role) {
            case "client": return CLIENT;
            case "manager": return MANAGER;
            case "admin": return ADMIN;
        }
        return null;
    }

    public String getName() {
        return role;
    }
}
