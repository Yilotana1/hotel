package com.example.hotel.model.service.exception;

public class ApartmentsNotFoundException extends ServiceException{
    public ApartmentsNotFoundException(final String message){
        super(message);
    }
}
