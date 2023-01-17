package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.dao.commons.Constants;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.ApplicationStatus;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ApplicationMapper implements EntityMapper<Application> {

    private final EntityMapper<Apartment> apartmentMapper;
    private final EntityMapper<User> userMapper;

    public ApplicationMapper(EntityMapper<Apartment> apartmentMapper, EntityMapper<User> userMapper) {
        this.apartmentMapper = apartmentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Application extractFromResultSet(final ResultSet rs) throws SQLException {
        final var startDate = Optional.ofNullable(rs.getDate(ColumnLabels.Application.START_DATE));
        final var endDate = Optional.ofNullable(rs.getDate(ColumnLabels.Application.END_DATE));
        return Application
                .builder()
                .id(rs.getLong(ColumnLabels.Application.ID))
                .applicationStatus(ApplicationStatus.getById(rs.getInt(ColumnLabels.Application.STATUS_ID)))
                .price(BigDecimal.valueOf(rs.getDouble(ColumnLabels.Application.PRICE)))
                .client(userMapper.extractFromResultSet(rs))
                .apartment(apartmentMapper.extractFromResultSet(rs))
                .creationDate(rs.getTimestamp(ColumnLabels.Application.CREATION_DATE).toLocalDateTime())
                .lastModified(rs.getTimestamp(ColumnLabels.Application.LAST_MODIFIED).toLocalDateTime())
                .stayLength(rs.getInt(ColumnLabels.Application.STAY_LENGTH))
                .startDate(startDate.map(Date::toLocalDate).orElse(null))
                .endDate(endDate.map(Date::toLocalDate).orElse(null))
                .build();
    }


}
