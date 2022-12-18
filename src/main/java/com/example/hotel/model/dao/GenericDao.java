package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface GenericDao<T> extends AutoCloseable {

    Connection getConnection() throws DaoException;

    int getCount() throws DaoException;

    int create(T entity) throws DaoException;

    Optional<T> findById(long id) throws DaoException;


    void update(T entity) throws DaoException;

    void delete(long id) throws DaoException;

    void close() throws SQLException;
}
