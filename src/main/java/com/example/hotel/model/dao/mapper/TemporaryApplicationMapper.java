package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.enums.ApartmentClass;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TemporaryApplicationMapper implements EntityMapper<TemporaryApplication> {
    @Override
    public TemporaryApplication extractFromResultSet(final ResultSet rs) throws SQLException {
        return TemporaryApplication
                .builder()
                .id(rs.getLong("temporary_application.id"))
                .apartmentClass(ApartmentClass.getById(rs.getInt("temporary_application.class_id")))
                .numberOfPeople(rs.getInt("temporary_application.number_of_people"))
                .stayLength(rs.getInt("temporary_application.stay_length"))
                .clientLogin(rs.getString("temporary_application.client_login"))
                .creationDate(rs.getDate("temporary_application.creation_date").toLocalDate())
                .build();
    }
}
