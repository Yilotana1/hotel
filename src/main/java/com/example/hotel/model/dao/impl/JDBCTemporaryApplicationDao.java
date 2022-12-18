package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.TemporaryApplication;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.example.hotel.model.dao.Tools.getGeneratedId;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.DELETE_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.DELETE_TEMPORARY_APPLICATION_BY_DELAY_FROM_CREATION_DATE;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.DELETE_TEMPORARY_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.INSERT_TEMPORARY_APPLICATION;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.SELECT_APARTMENT_REQUESTS_SORTED_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.SELECT_COUNT_TEMPORARY_APPLICATION;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.SELECT_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.SELECT_TEMPORARY_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.TemporaryApplicationSQL.UPDATE_APARTMENT_REQUEST;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCTemporaryApplicationDao implements TemporaryApplicationDao {
    public final static Logger log = Logger.getLogger(JDBCTemporaryApplicationDao.class);
    private final Connection connection;
    private final EntityMapper<TemporaryApplication> apartmentRequestMapper;

    public JDBCTemporaryApplicationDao(final Connection connection,
                                       final EntityMapper<TemporaryApplication> apartmentRequestMapper) {
        this.connection = connection;
        this.apartmentRequestMapper = apartmentRequestMapper;
    }

    @Override
    public void deleteByDaysFromCreationDate(final int daysFromCreation) throws DaoException {
        try (var deleteStatement = connection.prepareStatement(DELETE_TEMPORARY_APPLICATION_BY_DELAY_FROM_CREATION_DATE)) {
            deleteStatement.setInt(1, daysFromCreation);
            deleteStatement.executeUpdate();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            final var message = "Deleting temporary applications that were created more than"
                    + daysFromCreation + " two days ago" +
                    "failed during accessing database";
            throw new DaoException(message, e);
        }
    }

    @Override
    public Collection<TemporaryApplication> findSortedById(final int skip, final int count) throws DaoException {
        try (var selectTemporaryApplicationStatement = connection
                .prepareStatement(SELECT_APARTMENT_REQUESTS_SORTED_BY_NUMBER)) {
            selectTemporaryApplicationStatement.setInt(1, skip);
            selectTemporaryApplicationStatement.setInt(2, count);
            final var resultSet = selectTemporaryApplicationStatement.executeQuery();
            final var apartmentRequests = new ArrayList<TemporaryApplication>();
            while (resultSet.next()) {
                apartmentRequests.add(apartmentRequestMapper.extractFromResultSet(resultSet));
            }
            return apartmentRequests;
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting temporary applications" +
                    " sorted by id failed during accessing database", e);
        }
    }

    @Override
    public Optional<TemporaryApplication> findByClientLogin(final String clientLogin) throws DaoException {
        try (var selectTemporaryApplicationStatement = connection
                .prepareStatement(SELECT_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN)) {
            selectTemporaryApplicationStatement.setString(1, clientLogin);
            final var resultSet = selectTemporaryApplicationStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentRequestMapper.extractFromResultSet(resultSet));
            }
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting temporary application by login failed during accessing database", e);
        }
        return Optional.empty();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public int getCount() throws DaoException {
        try (var statement = connection.createStatement()) {
            final var resultSet = statement.executeQuery(SELECT_COUNT_TEMPORARY_APPLICATION);
            resultSet.next();
            return resultSet.getInt("count");
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Counting temporary applications" +
                    " failed during accessing data from database", e);
        }
    }

    @Override
    public int create(final TemporaryApplication temporaryApplication) throws DaoException {
        try (var insertTemporaryApplicationStatement = connection
                .prepareStatement(INSERT_TEMPORARY_APPLICATION, RETURN_GENERATED_KEYS)) {
            insertTemporaryApplicationStatement.setLong(1, temporaryApplication.getApartmentClass().getId());
            insertTemporaryApplicationStatement.setInt(2, temporaryApplication.getNumberOfPeople());
            insertTemporaryApplicationStatement.setInt(3, temporaryApplication.getStayLength());
            insertTemporaryApplicationStatement.setString(4, temporaryApplication.getClientLogin());
            insertTemporaryApplicationStatement.setDate(5, Date.valueOf(temporaryApplication.getCreationDate()));
            insertTemporaryApplicationStatement.executeUpdate();
            return getGeneratedId(insertTemporaryApplicationStatement);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Creating new temporary application record" +
                    " failed during accessing database", e);
        }
    }

    @Override
    public Optional<TemporaryApplication> findById(final long id) throws DaoException {
        try (var selectTemporaryApplicationStatement = connection.prepareStatement(SELECT_TEMPORARY_APPLICATION_BY_ID)) {
            selectTemporaryApplicationStatement.setLong(1, id);
            final var resultSet = selectTemporaryApplicationStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentRequestMapper.extractFromResultSet(resultSet));
            }
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting temporary application record by id" +
                    " failed during accessing database", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(final TemporaryApplication temporaryApplication) throws DaoException {
        try (var insertTemporaryApplicationStatement = connection.prepareStatement(UPDATE_APARTMENT_REQUEST)) {
            insertTemporaryApplicationStatement.setInt(1, temporaryApplication.getApartmentClass().getId());
            insertTemporaryApplicationStatement.setInt(2, temporaryApplication.getNumberOfPeople());
            insertTemporaryApplicationStatement.setInt(3, temporaryApplication.getStayLength());
            insertTemporaryApplicationStatement.setLong(4, temporaryApplication.getId());
            insertTemporaryApplicationStatement.executeUpdate();
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Updating temporary application record failed during accessing database", e);
        }
    }

    @Override
    public void delete(final long id) throws DaoException {
        try (var deleteTemporaryApplicationStatement = connection.prepareStatement(DELETE_TEMPORARY_APPLICATION_BY_ID)) {
            deleteTemporaryApplicationStatement.setLong(1, id);
            deleteTemporaryApplicationStatement.executeUpdate();
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Deleting temporary application record by id" +
                    " failed during accessing database", e);
        }
    }

    @Override
    public void delete(final String clientLogin) throws DaoException {
        try (var deleteTemporaryApplicationStatement = connection.prepareStatement(DELETE_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN)) {
            deleteTemporaryApplicationStatement.setString(1, clientLogin);
            deleteTemporaryApplicationStatement.executeUpdate();
        }catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Deleting temporary application" +
                    " record by clientLogin failed during accessing database", e);
        }
    }

    @Override
    public void close() throws SQLException {
        getConnection().close();
    }
}
