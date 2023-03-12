package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApartmentMapperTest {

  public static final String NON_NULL = "1";
  public static final String PRICE_STR = "312 $";
  public static final String NUMBER_STR = "1";
  private final UpdateApartmentDTO updateApartmentDTO = UpdateApartmentDTO
          .builder()
          .number(1L)
          .apartmentClass(ApartmentClass.SUITE)
          .price(PRICE_STR)
          .status(ApartmentStatus.BUSY)
          .build();

  private final ApartmentMapper mapper = new ApartmentMapper();

  @Test
  void givenCorrectRequest_map_shouldReturnUpdateApartmentDto() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(Constants.RequestParameters.BUSY_STATUS)).thenReturn(NON_NULL);
    when(request.getParameter(Constants.RequestParameters.PRICE)).thenReturn(PRICE_STR);
    when(request.getParameter(Constants.RequestParameters.APARTMENT_NUMBER)).thenReturn(NUMBER_STR);
    when(request.getParameter(Constants.RequestParameters.APARTMENT_CLASS_ID))
            .thenReturn(updateApartmentDTO.getApartmentClass().getId().toString());

    final var dto = mapper.map(request);

    assertThat(dto.getNumber()).isEqualTo(updateApartmentDTO.getNumber());
    assertThat(dto.getPrice()).isEqualTo(updateApartmentDTO.getPrice());
    assertThat(dto.getStatus()).isEqualTo(updateApartmentDTO.getStatus());
    assertThat(dto.getApartmentClass()).isEqualTo(updateApartmentDTO.getApartmentClass());
  }
}