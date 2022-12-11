package com.example.hotel.controller.exception;

public class ApplicationNotFoundException extends RuntimeException {

    public ApplicationNotFoundException(final String message) {
        super(message);
    }
}
