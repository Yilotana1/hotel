package com.example.hotel.controller.factory.validator.impl;

import com.example.hotel.controller.factory.validator.ValidatorFactory;
import com.example.hotel.controller.validator.impl.ApartmentValidator;
import com.example.hotel.controller.validator.impl.ApplicationValidator;
import com.example.hotel.controller.validator.impl.TemporaryApplicationValidator;
import com.example.hotel.controller.validator.impl.UserValidator;
import com.example.hotel.controller.validator.Validator;

public class ValidatorFactoryImpl extends ValidatorFactory {

  @Override
  public Validator createUserValidator() {
    return new UserValidator();
  }

  @Override
  public Validator createApartmentValidator() {
    return new ApartmentValidator();
  }

  @Override
  public Validator createApplicationValidator() {
    return new ApplicationValidator();
  }

  @Override
  public Validator createTemporaryApplicationValidator() {
    return new TemporaryApplicationValidator();
  }
}
