package com.example.hotel.model.dao.mapper;

import com.example.hotel.model.dao.commons.Constants;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.entity.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements EntityMapper<Role> {
    @Override
    public Role extractFromResultSet(final ResultSet resultSet) throws SQLException {
        return Role.getFromString(resultSet.getString(ColumnLabels.Role.ROLE));
    }
}
