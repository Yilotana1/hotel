package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApartmentMapper implements EntityMapper<Apartment>{
    @Override
    public Apartment extractFromResultSet(ResultSet rs) throws SQLException {
        return Apartment
                .builder()
                .number(rs.getInt("number"))
                .floor(rs.getInt("floor"))
                .demand(rs.getInt("demand"))
                .price(BigDecimal.valueOf(rs.getDouble("price")))
                .apartmentClass(ApartmentClass.getById(rs.getInt("class_id")))
                .apartmentStatus(ApartmentStatus.getById(rs.getInt("status_id")))
                .numberOfPeople(rs.getInt("number_of_people"))
                .build();
    }
}
