package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.ApplicationStatus;

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
    public Application extractFromResultSet(ResultSet rs) throws SQLException {
        final var startDate = Optional.ofNullable(rs.getDate("start_date"));
        final var endDate = Optional.ofNullable(rs.getDate("end_date"));
        return Application
                .builder()
                .id(rs.getLong("application.id"))
                .applicationStatus(ApplicationStatus.getById(rs.getInt("application.status_id")))
                .price(rs.getBigDecimal("application.price"))
                .client(userMapper.extractFromResultSet(rs))
                .apartment(apartmentMapper.extractFromResultSet(rs))
                .creationDate(rs.getTimestamp("application.creation_date").toLocalDateTime())
                .lastModified(rs.getTimestamp("application.last_modified").toLocalDateTime())
                .stayLength(rs.getInt("application.stay_length"))
                .startDate(startDate.map(Date::toLocalDate).orElse(null))
                .endDate(endDate.map(Date::toLocalDate).orElse(null))
                .build();
    }


}
