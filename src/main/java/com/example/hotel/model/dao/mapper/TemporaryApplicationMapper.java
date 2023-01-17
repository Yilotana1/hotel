package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.enums.ApartmentClass;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TemporaryApplicationMapper implements EntityMapper<TemporaryApplication> {
    @Override
    public TemporaryApplication extractFromResultSet(final ResultSet rs) throws SQLException {
        return TemporaryApplication
                .builder()
                .id(rs.getLong(ColumnLabels.TemporaryApplication.ID))
                .apartmentClass(ApartmentClass.getById(rs.getInt(ColumnLabels.TemporaryApplication.CLASS_ID)))
                .numberOfPeople(rs.getInt(ColumnLabels.TemporaryApplication.NUMBER_OF_PEOPLE))
                .stayLength(rs.getInt(ColumnLabels.TemporaryApplication.STAY_LENGTH))
                .clientLogin(rs.getString(ColumnLabels.TemporaryApplication.CLIENT_LOGIN))
                .creationDate(rs.getDate(ColumnLabels.TemporaryApplication.CREATION_DATE).toLocalDate())
                .build();
    }
}
