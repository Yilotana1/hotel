package com.example.hotel.model.service.exception;

import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.User;

public class NotEnoughMoneyToConfirmException extends RuntimeException {
    private final User client;
    private final Application application;

    public NotEnoughMoneyToConfirmException(final User client, final Application application) {
        this.application = application;
        this.client = client;
    }

    public User getClient() {
        return client;
    }

    public Application getApplication() {
        return application;
    }
}
