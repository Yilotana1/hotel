package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.entity.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements EntityMapper<Role> {
    @Override
    public Role extractFromResultSet(ResultSet resultSet) throws SQLException {
        return Role.getFromString(resultSet.getString("role"));
    }
}
