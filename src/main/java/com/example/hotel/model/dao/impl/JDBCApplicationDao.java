package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.commons.Constants;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.Application;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.example.hotel.model.dao.commons.Tools.getGeneratedId;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.DELETE_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.INSERT_APPLICATION;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_APPROVED_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_COUNT_APPLICATIONS;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_NOT_APPROVED_APPLICATION_BY_CLIENT_ID;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_NOT_APPROVED_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_NOT_CANCELED_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_OUTDATED_APPLICATIONS;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.UPDATE_APPLICATION;
import static com.example.hotel.model.entity.enums.ApplicationStatus.NOT_APPROVED;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCApplicationDao implements ApplicationDao {
    public final static Logger log = Logger.getLogger(JDBCApplicationDao.class);
    private final Connection connection;
    private final EntityMapper<Application> applicationMapper;

    public JDBCApplicationDao(Connection connection, EntityMapper<Application> applicationMapper) {
        this.connection = connection;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public int getCount() throws DaoException {
        try (var countStatement = connection.createStatement()) {
            final var resultSet = countStatement.executeQuery(SELECT_COUNT_APPLICATIONS);
            resultSet.next();
            return resultSet.getInt(ColumnLabels.COUNT);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Counting application records failed during accessing database", e);
        }
    }

    @Override
    public long create(final Application application) throws DaoException {
        try (var createApplicationStatement = connection.prepareStatement(INSERT_APPLICATION, RETURN_GENERATED_KEYS)) {
            setApplicationFieldsToCreateStatement(application, createApplicationStatement);
            createApplicationStatement.executeUpdate();
            return getGeneratedId(createApplicationStatement);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Creating new application record failed during accessing database", e);
        }
    }

    private static void setApplicationFieldsToCreateStatement(final Application application,
                                                              final PreparedStatement createApplicationStatement) throws SQLException {
        createApplicationStatement.setLong(1, application.getClient().getId());
        createApplicationStatement.setLong(2, application.getApartment().getNumber());
        createApplicationStatement.setLong(3, application.getStatus().getId());
        createApplicationStatement.setBigDecimal(4, application.getPrice());
        createApplicationStatement.setTimestamp(5, Timestamp.valueOf(application.getCreationDate()));
        createApplicationStatement.setTimestamp(6, Timestamp.valueOf(application.getLastModified()));
        final var startDate = application.getStartDate().map(Date::valueOf);
        final var endDate = application.getEndDate().map(Date::valueOf);
        createApplicationStatement.setDate(7, startDate.orElse(null));
        createApplicationStatement.setDate(8, endDate.orElse(null));
        createApplicationStatement.setInt(9, application.getStayLength());

    }

    @Override
    public void update(final Application application) throws DaoException {
        try (var updateApplicationStatement = connection.prepareStatement(UPDATE_APPLICATION)) {
            setApplicationFieldsToUpdateStatement(application, updateApplicationStatement);
            updateApplicationStatement.executeUpdate();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Updating application's record failed during accessing database");
        }
    }

    private static void setApplicationFieldsToUpdateStatement(final Application application,
                                                              final PreparedStatement updateApplicationStatement) throws SQLException {
        updateApplicationStatement.setLong(1, application.getClient().getId());
        updateApplicationStatement.setLong(2, application.getApartment().getNumber());
        updateApplicationStatement.setLong(3, application.getStatus().getId());
        updateApplicationStatement.setBigDecimal(4, application.getPrice());
        updateApplicationStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        final var startDate = application.getStartDate().map(Date::valueOf);
        final var endDate = application.getEndDate().map(Date::valueOf);
        updateApplicationStatement.setDate(6, startDate.orElse(null));
        updateApplicationStatement.setDate(7, endDate.orElse(null));
        updateApplicationStatement.setInt(8, application.getStayLength());
        updateApplicationStatement.setLong(9, application.getId());

    }

    @Override
    public void delete(final long id) throws DaoException {
        try (var deleteApplicationStatement = connection.prepareStatement(DELETE_APPLICATION_BY_ID)) {
            deleteApplicationStatement.setLong(1, id);
            deleteApplicationStatement.executeUpdate();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Deleting application's record by id failed during accessing database", e);
        }
    }

    @Override
    public Optional<Application> findById(final long id) throws DaoException {
        try (var selectApplicationStatement = connection.prepareStatement(SELECT_APPLICATION_BY_ID)) {
            selectApplicationStatement.setLong(1, id);
            final var resultSet = selectApplicationStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(applicationMapper.extractFromResultSet(resultSet));
            }
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting application's record by id" +
                    " failed during accessing data from database", e);
        }
        return Optional.empty();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public Optional<Application> findNotApprovedByClientId(long clientId) throws DaoException {
        try (var selectApplicationsStatement = connection.prepareStatement(SELECT_NOT_APPROVED_APPLICATION_BY_CLIENT_ID)) {
            selectApplicationsStatement.setLong(1, clientId);
            selectApplicationsStatement.setInt(2, NOT_APPROVED.getId());
            final var resultSet = selectApplicationsStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(applicationMapper.extractFromResultSet(resultSet));
            }
            return Optional.empty();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting not approved application record by client id" +
                    " failed during accessing data from database", e);
        }
    }

    @Override
    public Optional<Application> findNotApprovedByLogin(final String login) throws DaoException {
        return getApplicationByLogin(login, SELECT_NOT_APPROVED_APPLICATION_BY_CLIENT_LOGIN);
    }

    @Override
    public Optional<Application> findApprovedByLogin(final String login) throws DaoException {
        return getApplicationByLogin(login, SELECT_APPROVED_APPLICATION_BY_CLIENT_LOGIN);
    }

    @Override
    public Optional<Application> findNotCanceledByLogin(String login) throws DaoException {
        return getApplicationByLogin(login, SELECT_NOT_CANCELED_APPLICATION_BY_CLIENT_LOGIN);
    }

    @Override
    public Collection<Application> findFinishedApproved() throws DaoException {
        try (var statement = connection.createStatement()) {
            final var resultSet = statement.executeQuery(SELECT_OUTDATED_APPLICATIONS);
            final var applications = new ArrayList<Application>();
            while (resultSet.next()) {
                applications.add(applicationMapper.extractFromResultSet(resultSet));
            }
            return applications;
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting finished approved application failed during accessing database", e);
        }
    }

    private Optional<Application> getApplicationByLogin(final String login, final String sqlQuery) throws DaoException {
        try (var selectApplicationsStatement = connection.prepareStatement(sqlQuery)) {
            selectApplicationsStatement.setString(1, login);
            final var resultSet = selectApplicationsStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(applicationMapper.extractFromResultSet(resultSet));
            }
            return Optional.empty();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting application record by client login" +
                    " failed during accessing data from database", e);
        }
    }
}
