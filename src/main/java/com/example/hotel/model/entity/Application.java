package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentStatus;

public class Application {

    private Long id;
    private User client;
    private Apartment apartment;
    private ApartmentStatus apartmentStatus;

    public Application(Long id, User client, Apartment apartment, ApartmentStatus apartmentStatus) {
        this.id = id;
        this.client = client;
        this.apartment = apartment;
        this.apartmentStatus = apartmentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public ApartmentStatus getApplicationStatus() {
        return apartmentStatus;
    }

    public void setApplicationStatus(ApartmentStatus apartmentStatus) {
        this.apartmentStatus = apartmentStatus;
    }
}
