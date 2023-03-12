package com.example.hotel.controller.validator.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.validator.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

import static java.lang.Long.parseLong;

public class ApartmentValidator implements Validator {

  public final static Logger log = Logger.getLogger(ApartmentValidator.class);

  public static final int MIN_NUMBER = 0;
  public static final String NUMBER_REGEX = "[0-9]+";
  public static final String PRICE_REGEX = "[0-9.]+ (\\$|Грн)";
  public static final String APARTMENT_CLASS_REGEX = "[1-4]";
  public static final String WHITESPACE = " ";

  @Override
  public void throwIfNotValid(final HttpServletRequest request) {
    final var number = request.getParameter(Constants.RequestParameters.APARTMENT_NUMBER);
    final var classId = request.getParameter(Constants.RequestParameters.APARTMENT_CLASS_ID);
    final var price = request.getParameter(Constants.RequestParameters.PRICE);
    throwIfNulls(number, classId, price);
    if (!number.matches(NUMBER_REGEX)) {
      throw new InvalidDataException("Apartment number must not be empty and be a number",
              Constants.RequestParameters.APARTMENT_NUMBER);
    }
    if (!price.matches(PRICE_REGEX)) {
      throw new InvalidDataException("Price must not be empty and be double value",
              Constants.RequestParameters.PRICE);
    }
    if (!classId.matches(APARTMENT_CLASS_REGEX)) {
      throw new InvalidDataException("Apartment class id must be between 1 and 4", "class");
    }
    if (parseLong(number) <= MIN_NUMBER) {
      throw new InvalidDataException("Apartment number must be more than zero",
              Constants.RequestParameters.APARTMENT_NUMBER);
    }
    if (new BigDecimal(price.substring(0, price.indexOf(WHITESPACE))).compareTo(BigDecimal.ZERO) <= 0) {
      throw new InvalidDataException("Apartment price must be more than zero",
              Constants.RequestParameters.PRICE);
    }
  }
}
