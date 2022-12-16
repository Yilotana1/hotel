package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.TemporaryApplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hotel.model.dao.Tools.getGeneratedId;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.DELETE_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.DELETE_TEMPORARY_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.INSERT_TEMPORARY_APPLICATION;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.SELECT_APARTMENT_REQUESTS_SORTED_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.SELECT_APARTMENT_REQUEST_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.SELECT_COUNT_TEMPORARY_APPLICATION;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.SELECT_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.sql.mysql.ApartmentRequestSQL.UPDATE_APARTMENT_REQUEST;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCTemporaryApplicationDao implements TemporaryApplicationDao {
    private final Connection connection;
    private final EntityMapper<TemporaryApplication> apartmentRequestMapper;

    public JDBCTemporaryApplicationDao(final Connection connection,
                                       final EntityMapper<TemporaryApplication> apartmentRequestMapper) {
        this.connection = connection;
        this.apartmentRequestMapper = apartmentRequestMapper;
    }

    @Override
    public List<TemporaryApplication> findSortedById(final int skip, final int count) throws SQLException {
        try (var selectTemporaryApplicationStatement = connection
                .prepareStatement(SELECT_APARTMENT_REQUESTS_SORTED_BY_NUMBER)) {
            selectTemporaryApplicationStatement.setInt(1, skip);
            selectTemporaryApplicationStatement.setInt(2, count);
            var resultSet = selectTemporaryApplicationStatement.executeQuery();
            var apartmentRequests = new ArrayList<TemporaryApplication>();
            while (resultSet.next()) {
                apartmentRequests.add(apartmentRequestMapper.extractFromResultSet(resultSet));
            }
            return apartmentRequests;
        }
    }

    @Override
    public Optional<TemporaryApplication> findByClientLogin(final String clientLogin) throws SQLException {
        try (var selectTemporaryApplicationStatement = connection
                .prepareStatement(SELECT_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN)) {
            selectTemporaryApplicationStatement.setString(1, clientLogin);
            final var resultSet = selectTemporaryApplicationStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentRequestMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection;
    }

    @Override
    public int getCount() throws SQLException {
        try (var statement = connection.createStatement()) {
            final var resultSet = statement.executeQuery(SELECT_COUNT_TEMPORARY_APPLICATION);
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    @Override
    public int create(final TemporaryApplication temporaryApplication) throws SQLException {
        try (var insertTemporaryApplicationStatement = connection
                .prepareStatement(INSERT_TEMPORARY_APPLICATION, RETURN_GENERATED_KEYS)) {
            insertTemporaryApplicationStatement.setLong(1, temporaryApplication.getApartmentClass().getId());
            insertTemporaryApplicationStatement.setInt(2, temporaryApplication.getNumberOfPeople());
            insertTemporaryApplicationStatement.setInt(3, temporaryApplication.getStayLength());
            insertTemporaryApplicationStatement.setString(4, temporaryApplication.getClientLogin());
            insertTemporaryApplicationStatement.executeUpdate();
            return getGeneratedId(insertTemporaryApplicationStatement);
        }
    }

    @Override
    public Optional<TemporaryApplication> findById(final long id) throws SQLException {
        try (var selectTemporaryApplicationStatement = connection.prepareStatement(SELECT_APARTMENT_REQUEST_BY_ID)) {
            selectTemporaryApplicationStatement.setLong(1, id);
            var resultSet = selectTemporaryApplicationStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentRequestMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(final TemporaryApplication temporaryApplication) throws SQLException {
        try (var insertTemporaryApplicationStatement = connection.prepareStatement(UPDATE_APARTMENT_REQUEST)) {
            insertTemporaryApplicationStatement.setInt(1, temporaryApplication.getApartmentClass().getId());
            insertTemporaryApplicationStatement.setInt(2, temporaryApplication.getNumberOfPeople());
            insertTemporaryApplicationStatement.setInt(3, temporaryApplication.getStayLength());
            insertTemporaryApplicationStatement.setLong(4, temporaryApplication.getId());
            insertTemporaryApplicationStatement.executeUpdate();
        }
    }

    @Override
    public void delete(final long id) throws SQLException {
        try (var deleteTemporaryApplicationStatement = connection.prepareStatement(DELETE_TEMPORARY_APPLICATION_BY_ID)) {
            deleteTemporaryApplicationStatement.setLong(1, id);
            deleteTemporaryApplicationStatement.executeUpdate();
        }
    }

    @Override
    public void delete(final String clientLogin) throws SQLException {
        try (var deleteTemporaryApplicationStatement = connection.prepareStatement(DELETE_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN)) {
            deleteTemporaryApplicationStatement.setString(1, clientLogin);
            deleteTemporaryApplicationStatement.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        getConnection().close();
    }
}
