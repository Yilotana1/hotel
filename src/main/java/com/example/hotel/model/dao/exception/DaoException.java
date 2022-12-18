package com.example.hotel.model.dao.exception;

public class DaoException extends RuntimeException {
    public DaoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DaoException(final String message) {
        super(message);
    }
}
