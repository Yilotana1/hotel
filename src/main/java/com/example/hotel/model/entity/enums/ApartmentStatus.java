package com.example.hotel.model.entity.enums;

public enum ApartmentStatus {
    FREE,
    BUSY,
    BOOKED,
    UNAVAILABLE;

    private final Integer id = this.ordinal() + 1;

    public static ApartmentStatus getById(final Integer id) {
        switch (id) {
            case 1:
                return FREE;
            case 2:
                return BUSY;
            case 3:
                return BOOKED;
            case 4:
                return UNAVAILABLE;
        }
        return null;
    }

    public Integer getId() {
        return id;
    }
}
