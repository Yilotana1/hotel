package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApplicationStatus;

public class Application {

    private Long id;
    private User client;
    private Apartment apartment;
    private ApplicationStatus applicationStatus;

    public Application(Long id, User client, Apartment apartment, ApplicationStatus applicationStatus) {
        this.id = id;
        this.client = client;
        this.apartment = apartment;
        this.applicationStatus = applicationStatus;
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

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
