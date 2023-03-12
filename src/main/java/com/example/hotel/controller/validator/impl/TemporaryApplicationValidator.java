package com.example.hotel.controller.validator.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.validator.Validator;
import jakarta.servlet.http.HttpServletRequest;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class TemporaryApplicationValidator implements Validator {

  private static final Integer MAX_STAY_LENGTH = 100;
  private static final Integer MAX_NUMBER_OF_PEOPLE = 4;
  private static final int MIN_STAY_LENGTH = 0;
  private static final int MIN_NUMBER_OF_PEOPLE = 0;


  @Override
  public void throwIfNotValid(final HttpServletRequest request) {
    final var stayLength = request.getParameter(Constants.RequestParameters.STAY_LENGTH);
    final var apartmentClass = request.getParameter(Constants.RequestParameters.APARTMENT_CLASS_ID);
    final var numberOfPeople = request.getParameter(Constants.RequestParameters.NUMBER_OF_PEOPLE);
    throwIfNulls(stayLength, apartmentClass, numberOfPeople);

    if (stayLength.isEmpty() || parseInt(stayLength) <= MIN_STAY_LENGTH || parseInt(stayLength) > MAX_STAY_LENGTH) {
      final var message = format(
              "Stay Length must be between %d and %d",
              MIN_STAY_LENGTH, MAX_STAY_LENGTH);

      throw new InvalidDataException(message, "stay_length");
    }
    if (numberOfPeople.isEmpty() || parseInt(numberOfPeople) <= MIN_NUMBER_OF_PEOPLE
            || parseInt(numberOfPeople) > MAX_NUMBER_OF_PEOPLE) {
      final var message = format(
              "numberOfPeople must be between %d and %d",
              MIN_NUMBER_OF_PEOPLE,
              MAX_NUMBER_OF_PEOPLE);
      throw new InvalidDataException(message, Constants.RequestParameters.NUMBER_OF_PEOPLE);
    }
  }
}
