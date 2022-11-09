package com.example.hotel.model.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityMapper<T>{
    T extractFromResultSet(ResultSet resultSet) throws SQLException;
}
