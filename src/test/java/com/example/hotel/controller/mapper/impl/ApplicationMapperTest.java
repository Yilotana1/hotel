package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.dto.ApplicationDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationMapperTest {

  public static final String NUMBER_STR = "1";
  private final ApplicationDTO applicationDto = ApplicationDTO
          .builder()
          .clientLogin("login")
          .stayLength(3)
          .apartmentNumber(1L)
          .build();

  private final ApplicationMapper mapper = new ApplicationMapper();

  @Test
  void givenCorrectRequest_map_shouldReturnApplicationDto() {
    final var request = mock(HttpServletRequest.class);
    final var session = mock(HttpSession.class);
    when(request.getParameter(Constants.RequestParameters.STAY_LENGTH))
            .thenReturn(applicationDto.getStayLength().toString());
    when(request.getParameter(Constants.RequestParameters.APARTMENT_NUMBER))
            .thenReturn(NUMBER_STR);
    when(request.getSession()).thenReturn(session);
    when(session.getAttribute(SessionAttributes.LOGIN)).thenReturn(applicationDto.getClientLogin());

    final var dto = mapper.map(request);

    assertThat(dto.getClientLogin()).isEqualTo(applicationDto.getClientLogin());
    assertThat(dto.getApartmentNumber()).isEqualTo(applicationDto.getApartmentNumber());
    assertThat(dto.getStayLength()).isEqualTo(applicationDto.getStayLength());
  }
}