package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements EntityMapper<User> {
    @Override
    public User extractFromResultSet(ResultSet rs) throws SQLException {
        var userBuilder = User.builder()
                .id(rs.getLong("user.id"))
                .login(rs.getString("user.login"))
                .firstname(rs.getString("user.firstname"))
                .lastname(rs.getString("user.lastname"))
                .password(rs.getString("user.password"))
                .email(rs.getString("user.email"))
                .phone(rs.getString("user.phone"));
        return userBuilder.build();
    }
}
