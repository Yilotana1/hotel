package com.example.hotel.model.service.exception;


public class NotEnoughMoneyToConfirmException extends ServiceException {

    public NotEnoughMoneyToConfirmException(final String message){
        super(message);
    }
}
