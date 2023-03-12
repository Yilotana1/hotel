package com.example.hotel.controller.factory.mapper;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.controller.factory.mapper.impl.MapperFactoryImpl;
import com.example.hotel.controller.mapper.Mapper;

public abstract class MapperFactory {

  private static MapperFactory mapperFactory;

  public abstract Mapper<UserDTO> createUserMapper();

  public abstract Mapper<UpdateApartmentDTO> createApartmentMapper();

  public abstract Mapper<ApplicationDTO> createApplicationMapper();

  public abstract Mapper<TemporaryApplicationDTO> createTemporaryApplicationMapper();

  public static MapperFactory getInstance() {
    if (mapperFactory == null) {
      synchronized (MapperFactory.class) {
        if (mapperFactory == null) {
          final var temp = new MapperFactoryImpl();
          mapperFactory = temp;
        }
      }
    }
    return mapperFactory;
  }
}
