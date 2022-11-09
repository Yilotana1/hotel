package com.example.hotel.model.entity.enums;

public enum ApartmentClass {
    STANDARD("standard"), SUPERIOR("superior"), STUDIO("studio"), SUITE("suite");

    private final String apartmentClass;
    ApartmentClass(String apartmentClass){
        this.apartmentClass = apartmentClass;
    }

    public String getApartmentClass() {
        return apartmentClass;
    }
}
