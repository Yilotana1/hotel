package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ApartmentMapper implements EntityMapper<Apartment> {
    @Override
    public Apartment extractFromResultSet(final ResultSet rs) throws SQLException {
        return Apartment
                .builder()
                .number(rs.getLong(ColumnLabels.Apartment.NUMBER))
                .floor(rs.getInt(ColumnLabels.Apartment.FLOOR))
                .demand(rs.getInt(ColumnLabels.Apartment.DEMAND))
                .price(BigDecimal.valueOf(rs.getDouble(ColumnLabels.Apartment.PRICE)))
                .apartmentClass(ApartmentClass.getById(rs.getInt(ColumnLabels.Apartment.CLASS_ID)))
                .apartmentStatus(ApartmentStatus.getById(rs.getInt(ColumnLabels.Apartment.STATUS_ID)))
                .numberOfPeople(rs.getInt(ColumnLabels.Apartment.NUMBER_OF_PEOPLE))
                .build();
    }
}
