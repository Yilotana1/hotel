package com.example.hotel.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {

    Connection getConnection() throws SQLException;

    int getCount() throws SQLException;

    int create(T entity) throws SQLException;

    Optional<T> findById(long id) throws SQLException;


    void update(T entity) throws SQLException;

    void delete(long id) throws SQLException;

    void close() throws SQLException;
}
