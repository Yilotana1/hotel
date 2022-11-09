package com.example.hotel.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tools {

    private static final int ID_INDEX = 1;
    public static int getGeneratedId(PreparedStatement statement) throws SQLException {
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(ID_INDEX);
    }
}
