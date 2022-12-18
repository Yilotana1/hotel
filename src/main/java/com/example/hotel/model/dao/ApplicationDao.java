package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.entity.Application;
import java.util.Collection;
import java.util.Optional;

public interface ApplicationDao extends GenericDao<Application> {
    Optional<Application> findNotApprovedByClientId(long clientId) throws DaoException;

    Optional<Application> findNotApprovedByLogin(String login) throws DaoException;

    Optional<Application> findApprovedByLogin(String login) throws DaoException;

    Optional<Application> findNotCanceledByLogin(String login) throws DaoException;

    Collection<Application> findFinishedApproved() throws DaoException;
}
