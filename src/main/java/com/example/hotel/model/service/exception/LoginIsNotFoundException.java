package com.example.hotel.model.service.exception;

public class LoginIsNotFoundException extends ServiceException{
    public LoginIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginIsNotFoundException(String message) {
        super(message);
    }
}
