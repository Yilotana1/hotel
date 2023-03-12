package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.model.entity.enums.ApartmentClass;
import jakarta.servlet.http.HttpServletRequest;

import static com.example.hotel.model.entity.enums.ApartmentStatus.BUSY;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class ApartmentMapper implements Mapper<UpdateApartmentDTO> {
  @Override
  public UpdateApartmentDTO map(final HttpServletRequest request) {
    final var status = request.getParameter(Constants.RequestParameters.BUSY_STATUS) != null ? BUSY : null;
    final var price = request.getParameter(Constants.RequestParameters.PRICE);
    return UpdateApartmentDTO.builder()
            .number(parseLong(request.getParameter(Constants.RequestParameters.APARTMENT_NUMBER)))
            .apartmentClass(
                    ApartmentClass
                            .getById(parseInt(request.getParameter(Constants.RequestParameters.APARTMENT_CLASS_ID))))
            .status(status)
            .price(price)
            .build();
  }
}
