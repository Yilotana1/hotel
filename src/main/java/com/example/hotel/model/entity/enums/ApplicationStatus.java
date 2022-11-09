package com.example.hotel.model.entity.enums;

public enum ApplicationStatus {
    FREE("free"), BUSY("busy"), BOOKED("booked"), UNAVAILABLE("unavailable");

    private String applicationStatus;
    ApplicationStatus(String applicationStatus){
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }
}
