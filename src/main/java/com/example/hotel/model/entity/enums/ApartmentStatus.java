package com.example.hotel.model.entity.enums;

public enum ApartmentStatus {
    FREE("free"),
    BUSY("busy"),
    BOOKED("booked"),
    UNAVAILABLE("unavailable");

    private final Integer id = this.ordinal() + 1;

    public static ApartmentStatus getById(Integer id) {
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

    private final String statusName;
    ApartmentStatus(String applicationStatus){
        this.statusName = applicationStatus;
    }

    public String getStatusName() {
        return statusName;
    }
}
