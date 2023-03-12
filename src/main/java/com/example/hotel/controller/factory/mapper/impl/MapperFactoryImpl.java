package com.example.hotel.controller.factory.mapper.impl;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.controller.factory.mapper.MapperFactory;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.controller.mapper.impl.ApartmentMapper;
import com.example.hotel.controller.mapper.impl.ApplicationMapper;
import com.example.hotel.controller.mapper.impl.TemporaryApplicationMapper;
import com.example.hotel.controller.mapper.impl.UserMapper;

public class MapperFactoryImpl extends MapperFactory {
  @Override
  public Mapper<UserDTO> createUserMapper() {
    return new UserMapper();
  }

  @Override
  public Mapper<UpdateApartmentDTO> createApartmentMapper() {
    return new ApartmentMapper();
  }

  @Override
  public Mapper<ApplicationDTO> createApplicationMapper() {
    return new ApplicationMapper();
  }

  @Override
  public Mapper<TemporaryApplicationDTO> createTemporaryApplicationMapper() {
    return new TemporaryApplicationMapper();
  }
}
