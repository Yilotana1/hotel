package com.example.hotel.model.dao;

import com.example.hotel.model.entity.Application;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ApplicationDao extends GenericDao<Application> {
    Optional<Application> findNotApprovedByClientId(long clientId) throws SQLException;

    Optional<Application> findNotApprovedByLogin(String login) throws SQLException;

    Optional<Application> findApprovedByLogin(String login) throws SQLException;

    Optional<Application> findNotCanceledByLogin(String login) throws SQLException;

    List<Application> findOutdatedApproved() throws SQLException;
}
