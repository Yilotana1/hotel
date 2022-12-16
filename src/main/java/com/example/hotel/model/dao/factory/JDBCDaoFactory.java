package com.example.hotel.model.dao.factory;

import com.example.hotel.model.ConnectionPoolHolder;
import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.impl.JDBCApartmentDao;
import com.example.hotel.model.dao.impl.JDBCTemporaryApplicationDao;
import com.example.hotel.model.dao.impl.JDBCApplicationDao;
import com.example.hotel.model.dao.impl.JDBCUserDao;
import com.example.hotel.model.dao.mapper.ApartmentMapper;
import com.example.hotel.model.dao.mapper.TemporaryApplicationMapper;
import com.example.hotel.model.dao.mapper.ApplicationMapper;
import com.example.hotel.model.dao.mapper.RoleMapper;
import com.example.hotel.model.dao.mapper.UserMapper;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCDaoFactory extends  DaoFactory {

    private final DataSource dataSource = ConnectionPoolHolder.getDataSource();

    @Override
    public UserDao createUserDao() {
        return new JDBCUserDao(getConnection(), new UserMapper(), new RoleMapper());
    }

    @Override
    public TemporaryApplicationDao createTemporaryApplicationDao() {
        return new JDBCTemporaryApplicationDao(getConnection(), new TemporaryApplicationMapper());
    }

    @Override
    public TemporaryApplicationDao createTemporaryApplicationDao(final Connection connection) {
        return new JDBCTemporaryApplicationDao(connection, new TemporaryApplicationMapper());
    }

    @Override
    public UserDao createUserDao(Connection connection) {
         return new JDBCUserDao(connection, new UserMapper(), new RoleMapper());
    }

    @Override
    public ApartmentDao createApartmentDao() {
        return new JDBCApartmentDao(getConnection(), new ApartmentMapper());
    }

    @Override
    public ApartmentDao createApartmentDao(final Connection connection) {
        return new JDBCApartmentDao(connection, new ApartmentMapper());
    }

    @Override
    public ApplicationDao createApplicationDao() {
        return new JDBCApplicationDao(getConnection(), new ApplicationMapper(new ApartmentMapper(), new UserMapper()));
    }

    @Override
    public ApplicationDao createApplicationDao(final Connection connection) {
        return new JDBCApplicationDao(connection, new ApplicationMapper(new ApartmentMapper(), new UserMapper()));
    }

    @Override
    protected Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
