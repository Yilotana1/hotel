package com.example.hotel.model.service.exception;

public class ServiceException extends RuntimeException{

    public ServiceException(final String message, final Throwable cause){
        super(message, cause);
    }
    public ServiceException(final String message){
        super(message);
    }
    public ServiceException(){
    }
}
