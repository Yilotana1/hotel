package com.example.hotel.model.entity.enums;

public enum ApplicationStatus {
    APPROVED("approved"),
    NOT_APPROVED("not_approved"),
    CANCELED("canceled");
    private final String name;

    public static ApplicationStatus getById(final Integer id) {
        switch (id) {
            case 1:
                return APPROVED;
            case 2:
                return NOT_APPROVED;
            case 3:
                return CANCELED;
        }
        return null;
    }

    private final Integer id = this.ordinal() + 1;
    ApplicationStatus(final String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
