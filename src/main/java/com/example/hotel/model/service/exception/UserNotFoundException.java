package com.example.hotel.model.service.exception;

public class UserNotFoundException extends ServiceException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
