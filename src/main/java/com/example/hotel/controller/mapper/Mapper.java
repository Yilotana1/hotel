package com.example.hotel.controller.mapper;

import jakarta.servlet.http.HttpServletRequest;

public interface Mapper<T> {

  T map(HttpServletRequest request);
}
