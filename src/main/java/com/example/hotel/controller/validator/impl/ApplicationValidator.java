package com.example.hotel.controller.validator.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.validator.Validator;
import jakarta.servlet.http.HttpServletRequest;

import static java.lang.Long.parseLong;

public class ApplicationValidator implements Validator {
  private static final String NUMBER_REGEX = "[0-9]+";

  @Override
  public void throwIfNotValid(final HttpServletRequest request) {
    final var apartmentNumber = request.getParameter(Constants.RequestParameters.APARTMENT_NUMBER);
    final var stayLength = request.getParameter(Constants.RequestParameters.STAY_LENGTH);
    throwIfNulls(apartmentNumber, stayLength);

    if (!apartmentNumber.matches(NUMBER_REGEX)) {
      throw new InvalidDataException("apartmentNumber must be a number", Constants.RequestParameters.APARTMENT_NUMBER);
    }
    if (!stayLength.matches(NUMBER_REGEX)) {
      throw new InvalidDataException("stayLength must be a number", Constants.RequestParameters.STAY_LENGTH);
    }
    if (parseLong(apartmentNumber) <= 0) {
      throw new InvalidDataException("apartmentNumber must be more that 0", Constants.RequestParameters.APARTMENT_NUMBER);
    }
    if (parseLong(stayLength) <= 0) {
      throw new InvalidDataException("stayLength must be more that 0", Constants.RequestParameters.STAY_LENGTH);
    }
  }
}
