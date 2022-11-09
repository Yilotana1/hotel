package com.example.hotel.model;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolHolder {
    private static volatile DataSource dataSource;

    public static DataSource getDataSource() {

        if (dataSource == null) {
            synchronized (ConnectionPoolHolder.class) {
                if (dataSource == null) {
                    try {
                        Context context = (Context) new InitialContext().lookup("java:comp/env");
                        dataSource = (DataSource) context.lookup("jdbc/root");
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return dataSource;

    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
