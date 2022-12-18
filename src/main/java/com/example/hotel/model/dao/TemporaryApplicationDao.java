package com.example.hotel.model.dao;

import com.example.hotel.model.entity.TemporaryApplication;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TemporaryApplicationDao extends GenericDao<TemporaryApplication> {

    void deleteByDaysFromCreationDate(int delayInDays)throws SQLException;
    List<TemporaryApplication> findSortedById(int skip, int count) throws SQLException;

    Optional<TemporaryApplication> findByClientLogin(String clientLogin) throws SQLException;

    int create(TemporaryApplication temporaryApplication) throws SQLException;

    void update(TemporaryApplication temporaryApplication) throws SQLException;

    void delete(long id) throws SQLException;
    void delete(String clientLogin) throws SQLException;
}
