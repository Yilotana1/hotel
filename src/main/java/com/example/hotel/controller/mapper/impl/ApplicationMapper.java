package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.mapper.Mapper;
import jakarta.servlet.http.HttpServletRequest;

import static com.example.hotel.controller.commons.Tools.getLoginFromSession;
import static java.lang.Integer.parseInt;

public class ApplicationMapper implements Mapper<ApplicationDTO> {
  @Override
  public ApplicationDTO map(final HttpServletRequest request) {
    final var stayLength = request.getParameter(Constants.RequestParameters.STAY_LENGTH);
    if (stayLength == null || stayLength.isBlank()) {
      throw new InvalidDataException("stayLength must be specified", Constants.RequestParameters.STAY_LENGTH);
    }
    final var apartmentNumber = request.getParameter(Constants.RequestParameters.APARTMENT_NUMBER);
    if (apartmentNumber == null) {
      throw new InvalidDataException("apartmentNumber must be specified", Constants.RequestParameters.STAY_LENGTH);
    }
    var clientLogin = request.getParameter(Constants.RequestParameters.LOGIN);
    if (clientLogin == null || clientLogin.isEmpty()) {
      clientLogin = getLoginFromSession(request.getSession());
      if (clientLogin == null || clientLogin.isEmpty()) {
        throw new InvalidDataException("login must be specified", Constants.RequestParameters.LOGIN);
      }
    }
    return ApplicationDTO.builder()
            .apartmentNumber(parseInt(apartmentNumber))
            .clientLogin(clientLogin)
            .stayLength(parseInt(stayLength))
            .build();
  }
}
