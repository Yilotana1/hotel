package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.UserStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements EntityMapper<User> {
    @Override
    public User extractFromResultSet(final ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong(ColumnLabels.User.ID))
                .login(rs.getString(ColumnLabels.User.LOGIN))
                .firstname(rs.getString(ColumnLabels.User.FIRSTNAME))
                .lastname(rs.getString(ColumnLabels.User.LASTNAME))
                .password(rs.getString(ColumnLabels.User.PASSWORD))
                .email(rs.getString(ColumnLabels.User.EMAIL))
                .phone(rs.getString(ColumnLabels.User.PHONE))
                .status(UserStatus.getById(rs.getInt(ColumnLabels.User.STATUS_ID)))
                .money(BigDecimal.valueOf(rs.getDouble(ColumnLabels.User.MONEY)))
                .build();
    }
}
