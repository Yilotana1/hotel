package com.example.hotel.model.dao.commons;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Tools {
    private static final int ID_INDEX = 1;

    public static int getGeneratedId(final PreparedStatement statement) throws SQLException {
        final var keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(ID_INDEX);
    }
}
