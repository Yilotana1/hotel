package com.example.hotel.model.service.exception;

public class ApartmentNotAllowedToUpdateException extends ServiceException{
    public ApartmentNotAllowedToUpdateException(final String message){
        super(message);
    }
}
