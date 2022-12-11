package com.example.hotel.model.service.exception;

public class ApartmentIsNotAvailableException extends ServiceException{
    public ApartmentIsNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApartmentIsNotAvailableException() {
    }
}
