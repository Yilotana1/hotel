package com.example.hotel.controller.factory.validator;

import com.example.hotel.controller.factory.validator.impl.ValidatorFactoryImpl;
import com.example.hotel.controller.validator.Validator;

public abstract class ValidatorFactory {
  private static ValidatorFactory validatorFactory;

  public abstract Validator createUserValidator();
  public abstract Validator createApartmentValidator();
  public abstract Validator createApplicationValidator();
  public abstract Validator createTemporaryApplicationValidator();

  public static ValidatorFactory getInstance() {
    if (validatorFactory == null) {
      synchronized (ValidatorFactory.class) {
        if (validatorFactory == null) {
          final var temp = new ValidatorFactoryImpl();
          validatorFactory = temp;
        }
      }
    }
    return validatorFactory;
  }
}
