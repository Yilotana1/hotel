package com.example.hotel.model.entity.enums;

public enum Role {
    CLIENT("client"), MANAGER("manager");
    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role getFromString(String role) {
        switch (role) {
            case "client": return CLIENT;
            case "manager": return MANAGER;
        }
        return null;
    }

    public String getName() {
        return role;
    }
}
