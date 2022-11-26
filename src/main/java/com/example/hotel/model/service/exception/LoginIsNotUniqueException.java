package com.example.hotel.model.service.exception;

public class LoginIsNotUniqueException extends ServiceException{
    public LoginIsNotUniqueException(String message) {
        super(message);
    }
}
