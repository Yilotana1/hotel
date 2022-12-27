package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApartmentMapper implements EntityMapper<Apartment>{
    @Override
    public Apartment extractFromResultSet(final ResultSet rs) throws SQLException {
        return Apartment
                .builder()
                .number(rs.getInt("apartment.number"))
                .floor(rs.getInt("apartment.floor"))
                .demand(rs.getInt("apartment.demand"))
                .price(BigDecimal.valueOf(rs.getDouble("apartment.price")))
                .apartmentClass(ApartmentClass.getById(rs.getInt("apartment.class_id")))
                .apartmentStatus(ApartmentStatus.getById(rs.getInt("apartment.status_id")))
                .numberOfPeople(rs.getInt("apartment.number_of_people"))
                .build();
    }
}
