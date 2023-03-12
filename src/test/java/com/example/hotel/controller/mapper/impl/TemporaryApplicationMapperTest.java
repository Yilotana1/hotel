package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.model.entity.enums.ApartmentClass;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TemporaryApplicationMapperTest {

  private final TemporaryApplicationDTO temporaryApplicationDto = TemporaryApplicationDTO
          .builder()
          .clientLogin("login")
          .stayLength(3)
          .numberOfPeople(3)
          .apartmentClass(ApartmentClass.STANDARD)
          .build();

  private final TemporaryApplicationMapper mapper = new TemporaryApplicationMapper();


  @Test
  void givenCorrectRequest_map_shouldReturnTemporaryApplicationDto() {
    final var request = mock(HttpServletRequest.class);
    final var session = mock(HttpSession.class);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(SessionAttributes.LOGIN)).thenReturn(temporaryApplicationDto.getClientLogin());
    when(request.getParameter(Constants.RequestParameters.STAY_LENGTH))
            .thenReturn(temporaryApplicationDto.getStayLength().toString());
    when(request.getParameter(Constants.RequestParameters.APARTMENT_CLASS_ID))
            .thenReturn(temporaryApplicationDto.getApartmentClass().getId().toString());
    when(request.getParameter(Constants.RequestParameters.NUMBER_OF_PEOPLE))
            .thenReturn(temporaryApplicationDto.getNumberOfPeople().toString());


    final var dto = mapper.map(request);

    assertThat(dto.getClientLogin()).isEqualTo(temporaryApplicationDto.getClientLogin());
    assertThat(dto.getStayLength()).isEqualTo(temporaryApplicationDto.getStayLength());
    assertThat(dto.getNumberOfPeople()).isEqualTo(temporaryApplicationDto.getNumberOfPeople());
    assertThat(dto.getApartmentClass()).isEqualTo(temporaryApplicationDto.getApartmentClass());
  }
}