package com.example.hotel.controller.mapper.impl;

import com.example.hotel.controller.commons.Constants;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.controller.mapper.Mapper;
import jakarta.servlet.http.HttpServletRequest;

public class UserMapper implements Mapper<UserDTO> {
  @Override
  public UserDTO map(final HttpServletRequest request) {
    return UserDTO.builder()
            .login(request.getParameter(Constants.RequestParameters.LOGIN))
            .firstname(request.getParameter(Constants.RequestParameters.FIRSTNAME))
            .lastname(request.getParameter(Constants.RequestParameters.LASTNAME))
            .email(request.getParameter(Constants.RequestParameters.EMAIL))
            .password(request.getParameter(Constants.RequestParameters.PASSWORD))
            .phone(request.getParameter(Constants.RequestParameters.PHONE))
            .build();
  }
}
