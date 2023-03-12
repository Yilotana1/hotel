package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.model.entity.enums.ApartmentClass;
import jakarta.servlet.http.HttpServletRequest;

import static com.example.hotel.controller.commons.Tools.getLoginFromSession;
import static java.lang.Integer.parseInt;

public class TemporaryApplicationMapper implements Mapper<TemporaryApplicationDTO> {
  @Override
  public TemporaryApplicationDTO map(final HttpServletRequest request) {
    final var stayLength = request.getParameter(Constants.RequestParameters.STAY_LENGTH);
    final var apartmentClass = request.getParameter(Constants.RequestParameters.APARTMENT_CLASS_ID);
    final var numberOfPeople = request.getParameter(Constants.RequestParameters.NUMBER_OF_PEOPLE);
    return TemporaryApplicationDTO.builder()
            .stayLength(parseInt(stayLength))
            .apartmentClass(ApartmentClass.getById(parseInt(apartmentClass)))
            .numberOfPeople(parseInt(numberOfPeople))
            .clientLogin(getLoginFromSession(request.getSession()))
            .build();
  }
}
