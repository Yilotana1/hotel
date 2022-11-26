package com.example.hotel.model.service.exception;

public class WrongUserCredentialsException extends ServiceException {
    public WrongUserCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
    public WrongUserCredentialsException(String message) {
        super(message);
    }
}
