package com.example.hotel.controller.exception;

public class InvalidDataException extends RuntimeException {

    private String invalidField;

    public InvalidDataException(String message, String invalidField) {
        super(message);
        this.invalidField = invalidField;
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public String getInvalidField() {
        return invalidField;
    }
}
