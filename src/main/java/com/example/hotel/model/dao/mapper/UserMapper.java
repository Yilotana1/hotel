package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements EntityMapper<User> {
    @Override
    public User extractFromResultSet(ResultSet rs) throws SQLException {
        var userBuilder = User.builder()
                .id(rs.getLong("user.id"))
                .login(rs.getString("login"))
                .firstname(rs.getString("firstname"))
                .lastname(rs.getString("lastname"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"));
        return userBuilder.build();
    }
}
