package com.example.hotel.controller.validator.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.validator.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class UserValidator implements Validator {

  public final static Logger log = Logger.getLogger(UserValidator.class);
  private static final int MAX_LOGIN_LENGTH = 16;
  private static final int MAX_FIRSTNAME_LENGTH = 20;
  private static final int MAX_LASTNAME_LENGTH = 20;
  private static final int MAX_PHONE_LENGTH = 45;
  private static final String EMAIL_REGEX = "^(.+)@(.+)$";
  private static final String FIRSTNAME_REGEX = "^[\\p{IsCyrillic}A-Za-z]+$";
  private static final String LASTNAME_REGEX = "^[\\p{IsCyrillic}A-Za-z]+$";
  private static final String PHONE_REGEX = "^\\+\\d+$";
  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final int MAX_PASSWORD_LENGTH = 32;

  @Override
  public void throwIfNotValid(final HttpServletRequest request) {
    final var login = request.getParameter(Constants.RequestParameters.LOGIN);
    final var firstname = request.getParameter(Constants.RequestParameters.FIRSTNAME);
    final var lastname = request.getParameter(Constants.RequestParameters.LASTNAME);
    final var email = request.getParameter(Constants.RequestParameters.EMAIL);
    final var password = request.getParameter(Constants.RequestParameters.PASSWORD);
    final var phone = request.getParameter(Constants.RequestParameters.PHONE);
    throwIfNulls(login, firstname, lastname, email, password, phone);

    if (login.length() > MAX_LOGIN_LENGTH || login.isEmpty()) {
      throw new InvalidDataException("Login must be of appropriate size", "login");
    }
    if (firstname.length() > MAX_FIRSTNAME_LENGTH || !firstname.matches(FIRSTNAME_REGEX)) {
      throw new InvalidDataException("Firstname must not be too long and must only have letters", "firstname");
    }
    if (lastname.length() > MAX_LASTNAME_LENGTH || !lastname.matches(LASTNAME_REGEX)) {
      throw new InvalidDataException("Lastname must not be too long and must only have letters", "lastname");
    }
    if ((email != null && email.isEmpty()) || (email != null && !email.matches(EMAIL_REGEX))) {
      throw new InvalidDataException("Email must follow typical pattern", "email");
    }
    if (phone.length() > MAX_PHONE_LENGTH || !phone.matches(PHONE_REGEX)) {
      throw new InvalidDataException("Phone must only have digits and must not be too long", "phone");
    }
    if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
      throw new InvalidDataException("Password is incorrect", "password");
    }

  }
}
