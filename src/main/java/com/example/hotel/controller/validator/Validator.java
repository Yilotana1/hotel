package com.example.hotel.controller.validator;

import com.example.hotel.controller.exception.InvalidDataException;
import jakarta.servlet.http.HttpServletRequest;

import static java.util.Objects.requireNonNull;

public interface Validator {

  void throwIfNotValid(HttpServletRequest request);




  default void throwIfNulls(final String... args) throws InvalidDataException {
    try {
      for (final var arg : args) {
        requireNonNull(arg);
      }
    } catch (final NullPointerException e) {
      final var message = "Some of the required parameters for Dto are missing.";
      throw new InvalidDataException(message);
    }
  }
}
