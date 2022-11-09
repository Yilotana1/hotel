package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements EntityMapper<User> {
    @Override
    public User extractFromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .firstname(rs.getString("firstname"))
                .lastname(rs.getString("lastname"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .build();
    }
}
