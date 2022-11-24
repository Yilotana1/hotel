package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.ApplicationStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationMapper implements EntityMapper<Application> {

    private final EntityMapper<Apartment> apartmentMapper;
    private final EntityMapper<User> userMapper;

    public ApplicationMapper(EntityMapper<Apartment> apartmentMapper, EntityMapper<User> userMapper) {
        this.apartmentMapper = apartmentMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Application extractFromResultSet(ResultSet rs) throws SQLException {
        return Application
                .builder()
                .id(rs.getLong("application.id"))
                .applicationStatus(ApplicationStatus.getById(rs.getInt("application.status_id")))
                .price(rs.getBigDecimal("application.price"))
                .client(userMapper.extractFromResultSet(rs))
                .apartment(apartmentMapper.extractFromResultSet(rs))
                .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                .lastModified(rs.getTimestamp("last_modified").toLocalDateTime())
                .startDate(rs.getTimestamp("start_date").toLocalDateTime())
                .endDate(rs.getTimestamp("end_date").toLocalDateTime())
                .build();
    }


}
