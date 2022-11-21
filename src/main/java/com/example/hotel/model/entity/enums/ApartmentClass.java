package com.example.hotel.model.entity.enums;

public enum ApartmentClass {
    STANDARD("standard", 4),
    SUPERIOR("superior", 3),
    STUDIO("studio", 2),
    SUITE("suite", 1);

    private final Integer id = this.ordinal() + 1;
    private final String name;
    private final int priority;

    ApartmentClass(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public static ApartmentClass getById(Integer id) {
        switch (id) {
            case 1:
                return STANDARD;
            case 2:
                return SUPERIOR;
            case 3:
                return STUDIO;
            case 4:
                return SUITE;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }
}
