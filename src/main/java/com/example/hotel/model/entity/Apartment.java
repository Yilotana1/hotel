package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentClass;

public class Apartment {
    private Long number;
    private Long floor;
    private ApartmentClass apartmentClass;
    private Integer demand;

    public Apartment(Long number, Long floor, ApartmentClass apartmentClass, Integer demand) {
        this.number = number;
        this.floor = floor;
        this.apartmentClass = apartmentClass;
        this.demand = demand;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getFloor() {
        return floor;
    }

    public void setFloor(Long floor) {
        this.floor = floor;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(ApartmentClass apartmentClass) {
        this.apartmentClass = apartmentClass;
    }

    public Integer getDemand() {
        return demand;
    }

    public void setDemand(Integer demand) {
        this.demand = demand;
    }
}
