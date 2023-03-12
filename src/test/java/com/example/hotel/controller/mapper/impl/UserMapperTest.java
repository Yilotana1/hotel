package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMapperTest {

  private final UserDTO userDto = UserDTO
          .builder()
          .login("login")
          .phone("+312312321")
          .email("login@gmail.com")
          .lastname("lastname")
          .firstname("firstname")
          .password("odkaspoaskdopsak")
          .build();

  private final UserMapper mapper = new UserMapper();

  @Test
  void givenCorrectRequest_map_shouldReturnUserDto() {
    final var request = mock(HttpServletRequest.class);
    when(request.getParameter(Constants.RequestParameters.LOGIN)).thenReturn(userDto.getLogin());
    when(request.getParameter(Constants.RequestParameters.FIRSTNAME)).thenReturn(userDto.getFirstname());
    when(request.getParameter(Constants.RequestParameters.LASTNAME)).thenReturn(userDto.getLastname());
    when(request.getParameter(Constants.RequestParameters.EMAIL)).thenReturn(userDto.getEmail());
    when(request.getParameter(Constants.RequestParameters.PHONE)).thenReturn(userDto.getPhone());
    when(request.getParameter(Constants.RequestParameters.PASSWORD)).thenReturn(userDto.getPassword());

    final var dto = mapper.map(request);

    assertThat(dto.getLogin()).isEqualTo(userDto.getLogin());
    assertThat(dto.getFirstname()).isEqualTo(userDto.getFirstname());
    assertThat(dto.getLastname()).isEqualTo(userDto.getLastname());
    assertThat(dto.getEmail()).isEqualTo(userDto.getEmail());
    assertThat(dto.getPhone()).isEqualTo(userDto.getPhone());
    assertThat(dto.getPassword()).isEqualTo(userDto.getPassword());
  }
}