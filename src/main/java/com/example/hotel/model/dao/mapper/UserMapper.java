package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.UserStatus;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements EntityMapper<User> {
    @Override
    public User extractFromResultSet(final ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("user.id"))
                .login(rs.getString("user.login"))
                .firstname(rs.getString("user.firstname"))
                .lastname(rs.getString("user.lastname"))
                .password(rs.getString("user.password"))
                .email(rs.getString("user.email"))
                .phone(rs.getString("user.phone"))
                .status(UserStatus.getById(rs.getInt("user.status_id")))
                .money(BigDecimal.valueOf(rs.getDouble("user.money")))
                .build();
    }
}
