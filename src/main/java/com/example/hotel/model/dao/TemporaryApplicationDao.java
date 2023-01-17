package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.entity.TemporaryApplication;

import java.util.Collection;
import java.util.Optional;

public interface TemporaryApplicationDao extends GenericDao<TemporaryApplication> {

    void deleteByDaysFromCreationDate(int delayInDays) throws DaoException;

    Collection<TemporaryApplication> findSortedById(int skip, int count) throws DaoException;

    Optional<TemporaryApplication> findByClientLogin(String clientLogin) throws DaoException;

    long create(TemporaryApplication temporaryApplication) throws DaoException;

    void update(TemporaryApplication temporaryApplication) throws DaoException;

    void delete(long id) throws DaoException;

    void delete(String clientLogin) throws DaoException;
}
