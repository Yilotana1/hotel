package com.example.hotel.model.dao.commons;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tools {
    public static final int ID_INDEX = 1;

    public static long getGeneratedId(final PreparedStatement statement) throws SQLException {
        final var keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getLong(ID_INDEX);
    }
}
