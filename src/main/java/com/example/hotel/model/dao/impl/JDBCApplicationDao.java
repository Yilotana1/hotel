package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.enums.ApplicationStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hotel.model.dao.Tools.getGeneratedId;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.DELETE_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.INSERT_APPLICATION;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_BY_APARTMENT_ID;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_BY_CLIENT_ID;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_BY_STATUS_ID;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_CREATION_DATE;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_CREATION_DATE_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_END_DATE;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_END_DATE_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_LAST_MODIFIED_DATE;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_LAST_MODIFIED_DATE_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_START_DATE;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATIONS_SORTED_BY_START_DATE_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.SELECT_COUNT_APPLICATIONS;
import static com.example.hotel.model.dao.sql.mysql.ApplicationSQL.UPDATE_APPLICATION;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCApplicationDao implements ApplicationDao {
    private final Connection connection;
    private final EntityMapper<Application> applicationMapper;

    public JDBCApplicationDao(Connection connection, EntityMapper<Application> applicationMapper) {
        this.connection = connection;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public Connection getConnection(){
        return connection;
    }

    @Override
    public int getCount() throws SQLException {
        try (var countStatement = connection.createStatement()) {
            var resultSet = countStatement.executeQuery(SELECT_COUNT_APPLICATIONS);
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    @Override
    public int create(Application application) throws SQLException {
        try (var createApplicationStatement = connection.prepareStatement(INSERT_APPLICATION, RETURN_GENERATED_KEYS)) {
            createApplicationStatement.setLong(1, application.getClient().getId());
            createApplicationStatement.setLong(2, application.getApartment().getNumber());
            createApplicationStatement.setLong(3, application.getStatus().getId());
            createApplicationStatement.setBigDecimal(4, application.getPrice());
            createApplicationStatement.setTimestamp(5, Timestamp.valueOf(application.getCreationDate()));
            createApplicationStatement.setTimestamp(6, Timestamp.valueOf(application.getLastModified()));
            createApplicationStatement.setTimestamp(7, Timestamp.valueOf(application.getStartDate()));
            createApplicationStatement.setTimestamp(8, Timestamp.valueOf(application.getEndDate()));
            createApplicationStatement.executeUpdate();
            return getGeneratedId(createApplicationStatement);
        }
    }

    @Override
    public void update(Application application) throws SQLException {
        try (var updateApplicationStatement = connection.prepareStatement(UPDATE_APPLICATION)) {
            updateApplicationStatement.setLong(1, application.getClient().getId());
            updateApplicationStatement.setLong(2, application.getApartment().getNumber());
            updateApplicationStatement.setLong(3, application.getStatus().getId());
            updateApplicationStatement.setBigDecimal(4, application.getPrice());
            updateApplicationStatement.setTimestamp(5, Timestamp.valueOf(application.getLastModified()));
            updateApplicationStatement.setTimestamp(6, Timestamp.valueOf(application.getStartDate()));
            updateApplicationStatement.setTimestamp(7, Timestamp.valueOf(application.getEndDate()));
            updateApplicationStatement.setLong(8, application.getId());
            updateApplicationStatement.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        try (var deleteApplicationStatement = connection.prepareStatement(DELETE_APPLICATION_BY_ID)) {
            deleteApplicationStatement.setLong(1, id);
            deleteApplicationStatement.executeUpdate();
        }
    }

    @Override
    public Optional<Application> findById(long id) throws SQLException {
        try (var selectApplicationStatement = connection.prepareStatement(SELECT_APPLICATION_BY_ID)) {
            selectApplicationStatement.setLong(1, id);
            var resultSet = selectApplicationStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(applicationMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    @Override
    public List<Application> findByClientId(long clientId, int skip, int count) throws SQLException {
        try (var selectApplicationsStatement = connection.prepareStatement(SELECT_APPLICATIONS_BY_CLIENT_ID)) {
            selectApplicationsStatement.setLong(1, clientId);
            return getListOfApplications(skip, count, selectApplicationsStatement);
        }
    }

    private List<Application> getSortedListOfApartments(int skip, int count, String sortingSql) throws SQLException {
        try (var selectApplicationsStatement = connection.prepareStatement(sortingSql)) {
            selectApplicationsStatement.setLong(1, skip);
            selectApplicationsStatement.setLong(2, count);
            var resultSet = selectApplicationsStatement.executeQuery();
            var applications = new ArrayList<Application>();
            while (resultSet.next()) {
                applications.add(applicationMapper.extractFromResultSet(resultSet));
            }
            return applications;
        }
    }

    @Override
    public List<Application> findSortedById(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_ID);
    }

    @Override
    public List<Application> findSortedByCreationDate(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_CREATION_DATE);
    }

    @Override
    public List<Application> findSortedByCreationDateDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_CREATION_DATE_DESC);
    }

    @Override
    public List<Application> findSortedByLastModification(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_LAST_MODIFIED_DATE);
    }

    @Override
    public List<Application> findSortedByLastModificationDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_LAST_MODIFIED_DATE_DESC);
    }


    @Override
    public List<Application> findSortedByStartDate(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_START_DATE);
    }

    @Override
    public List<Application> findSortedByStartDateDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_START_DATE_DESC);
    }


    @Override
    public List<Application> findSortedByEndDate(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_END_DATE);
    }

    @Override
    public List<Application> findSortedByEndDateDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APPLICATIONS_SORTED_BY_END_DATE_DESC);
    }


    @Override
    public List<Application> findByStatus(ApplicationStatus status, int skip, int count) throws SQLException {
        try (var selectApplicationsStatement = connection.prepareStatement(SELECT_APPLICATIONS_BY_STATUS_ID)) {
            selectApplicationsStatement.setLong(1, status.getId());
            return getListOfApplications(skip, count, selectApplicationsStatement);
        }
    }


    @Override
    public List<Application> findByApartmentId(long apartmentId, int skip, int count) throws SQLException {
        try (var selectApplicationsStatement = connection.prepareStatement(SELECT_APPLICATIONS_BY_APARTMENT_ID)) {
            selectApplicationsStatement.setLong(1, apartmentId);
            return getListOfApplications(skip, count, selectApplicationsStatement);
        }
    }

    private List<Application> getListOfApplications(int skip, int count, PreparedStatement selectApplicationsStatement) throws SQLException {
        selectApplicationsStatement.setLong(2, skip);
        selectApplicationsStatement.setLong(3, count);
        var resultSet = selectApplicationsStatement.executeQuery();
        var applications = new ArrayList<Application>();
        while (resultSet.next()) {
            applications.add(applicationMapper.extractFromResultSet(resultSet));
        }
        return applications;
    }
}
