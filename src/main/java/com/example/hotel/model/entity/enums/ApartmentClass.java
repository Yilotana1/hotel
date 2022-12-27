package com.example.hotel.model.entity.enums;

public enum ApartmentClass {
    STANDARD("standard"),
    SUPERIOR("superior"),
    STUDIO("studio"),
    SUITE("suite");

    private final Integer id = this.ordinal() + 1;
    private final String name;

    ApartmentClass(final String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public static ApartmentClass getById(final Integer id) {
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
}
