package com.example.hotel.model.dao.factory;

import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.UserDao;

import java.sql.Connection;

public abstract class DaoFactory {
    private static DaoFactory daoFactory;

    public abstract UserDao createUserDao();

    public abstract TemporaryApplicationDao createTemporaryApplicationDao();

    public abstract TemporaryApplicationDao createTemporaryApplicationDao(Connection connection);

    public abstract UserDao createUserDao(Connection connection);

    public abstract ApartmentDao createApartmentDao();

    public abstract ApartmentDao createApartmentDao(Connection connection);

    public abstract ApplicationDao createApplicationDao();

    public abstract ApplicationDao createApplicationDao(Connection connection);

    public static DaoFactory getInstance() {
        if (daoFactory == null) {
            synchronized (DaoFactory.class) {
                if (daoFactory == null) {
                    final var temp = new JDBCDaoFactory();
                    daoFactory = temp;
                }
            }
        }
        return daoFactory;
    }

    protected abstract Connection getConnection();
}
