package com.example.hotel.model.service.exception;

public class ApartmentNotFoundException extends ServiceException {
    public ApartmentNotFoundException(final String message) {
        super(message);
    }
}
