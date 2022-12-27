package com.example.hotel.model.entity.enums;

public enum UserStatus {

    BLOCKED("blocked"), NON_BLOCKED("non_blocked");
    private final String name;
    private final Integer id = this.ordinal() + 1;

    UserStatus(final String name) {
        this.name = name;
    }

    public static UserStatus getById(final Integer id) {
        switch (id) {
            case 1:
                return BLOCKED;
            case 2:
                return NON_BLOCKED;
        }
        return null;
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
