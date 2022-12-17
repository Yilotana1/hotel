package com.example.hotel.model.service.exception;

public interface Messages {
    String SERVICE_EXCEPTION = "Something went wrong on the service layer.";
    String LOGIN_IS_NOT_UNIQUE = "Specified login = %s is already present.";
    String USER_WITH_SUCH_ID_NOT_FOUND = "User with id = %d not found";
    String NO_APPLICATION_FOUND = "Application with id = %d not found";
    String USER_WITH_SUCH_LOGIN_NOT_FOUND = "User with login = %s is not found.";
    String UPDATING_USERS_MONEY_FAILED = "Updating user's money process failed";
    String TEMPORARY_APPLICATION_NOT_FOUND = "TemporaryApplication not found";
}
