package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.enums.ApartmentClass;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.DELETE_APARTMENT_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.INSERT_APARTMENT;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_AND_RESIDENTS_LOGINS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_BY_CLIENT_PREFERENCES;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_CLASS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_PRICE;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_STATUS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENT_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENT_BY_RESIDENT_LOGIN;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_COUNT_APARTMENTS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_COUNT_APARTMENT_BY_CLASS_AND_NUMBER_OF_PEOPLE;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_COUNT_PREFERRED_APARTMENTS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.UPDATE_APARTMENT;

public class JDBCApartmentDao implements ApartmentDao {
    public final static Logger log = Logger.getLogger(JDBCApartmentDao.class);
    private final Connection connection;
    private final EntityMapper<Apartment> apartmentMapper;

    public JDBCApartmentDao(Connection connection, EntityMapper<Apartment> apartmentMapper) {
        this.connection = connection;
        this.apartmentMapper = apartmentMapper;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Map<Apartment, String> findApartmentsWithResidentsLogins(final int skip, final int count) throws DaoException {
        try (var statement = connection.prepareStatement(SELECT_APARTMENTS_AND_RESIDENTS_LOGINS)) {
            statement.setInt(1, skip);
            statement.setInt(2, count);
            final var resultSet = statement.executeQuery();
            final var map = new LinkedHashMap<Apartment, String>();
            while (resultSet.next()) {
                final var apartment = apartmentMapper.extractFromResultSet(resultSet);
                final var login = resultSet.getString("User.login");
                map.put(apartment, login);
            }
            return map;
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting Map(apartments, its residents)" +
                    " failed during accessing data from database", e);
        }
    }

    @Override
    public Optional<Apartment> findApartmentByResidentLogin(final String login) throws DaoException {
        try (var statement = connection.prepareStatement(SELECT_APARTMENT_BY_RESIDENT_LOGIN)) {
            final var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentMapper.extractFromResultSet(resultSet));
            }
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting apartment's record by login" +
                    " failed during accessing data from database", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByClassAndNumberOfPeople(final ApartmentClass apartmentClass,
                                                  final int numberOfPeople) throws DaoException {
        try (var statement = connection
                .prepareStatement(SELECT_COUNT_APARTMENT_BY_CLASS_AND_NUMBER_OF_PEOPLE)) {
            statement.setInt(1, apartmentClass.getId());
            statement.setInt(2, numberOfPeople);
            final var resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count") > 0;
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Checking if apartment record exists by number of people and class" +
                    " failed during accessing data from database", e);
        }
    }

    @Override
    public int getPreferredApartmentsCount(final String clientLogin) throws DaoException {
        try (var statement = connection.prepareStatement(SELECT_COUNT_PREFERRED_APARTMENTS)) {
            statement.setString(1, clientLogin);
            final var resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("count");
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting number of apartment records by client login " +
                    "failed during accessing data from database", e);
        }
    }

    @Override
    public Collection<Apartment> findByTemporaryApplication(final TemporaryApplication temporaryApplication,
                                                            final int skip,
                                                            final int count) throws DaoException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENTS_BY_CLIENT_PREFERENCES)) {
            selectApartmentStatement.setInt(1, temporaryApplication.getNumberOfPeople());
            selectApartmentStatement.setInt(2, temporaryApplication.getApartmentClass().getId());
            selectApartmentStatement.setInt(3, skip);
            selectApartmentStatement.setInt(4, count);
            return getApartmentsFromStatement(selectApartmentStatement);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting apartment record by temporary application failed " +
                    "during accessing data from database", e);
        }
    }

    @Override
    public Optional<Apartment> findByNumber(final long number) throws DaoException {
        return findById(number);
    }

    @Override
    public Collection<Apartment> findSortedByClass(final int skip, final int count) throws DaoException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_CLASS);
    }

    @Override
    public Collection<Apartment> findSortedByStatus(final int skip, final int count) throws DaoException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_STATUS);
    }

    @Override
    public Collection<Apartment> findSortedByPrice(final int skip, final int count) throws DaoException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_PRICE);
    }

    @Override
    public Collection<Apartment> findSortedByNumberOfPeople(final int skip, final int count) throws DaoException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE);
    }

    @Override
    public int getCount() throws DaoException {
        try (var statement = connection.createStatement()) {
            final var resultSet = statement.executeQuery(SELECT_COUNT_APARTMENTS);
            resultSet.next();
            return resultSet.getInt("count");
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Counting apartment records failed during accessing data from database", e);
        }
    }


    @Override
    public Optional<Apartment> findById(final long id) throws DaoException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENT_BY_NUMBER)) {
            selectApartmentStatement.setLong(1, id);
            var resultSet = selectApartmentStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentMapper.extractFromResultSet(resultSet));
            }
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting apartment record by id" +
                    " failed during accessing data from database", e);
        }
        return Optional.empty();
    }

    @Override
    public int create(final Apartment apartment) throws DaoException {
        try (var insertApartmentStatement = connection.prepareStatement(INSERT_APARTMENT)) {
            insertApartmentStatement.setLong(1, apartment.getNumber());
            insertApartmentStatement.setLong(2, apartment.getFloor());
            insertApartmentStatement.setLong(3, apartment.getApartmentClass().getId());
            insertApartmentStatement.setLong(4, apartment.getStatus().getId());
            insertApartmentStatement.setInt(5, apartment.getDemand());
            insertApartmentStatement.setBigDecimal(6, apartment.getPrice());
            insertApartmentStatement.setInt(7, apartment.getNumberOfPeople());
            insertApartmentStatement.executeUpdate();
            return apartment.getNumber();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Creating new apartment's record failed during accessing database", e);
        }
    }

    @Override
    public void update(final Apartment apartment) throws DaoException {
        try (var insertApartmentStatement = connection.prepareStatement(UPDATE_APARTMENT)) {
            insertApartmentStatement.setLong(1, apartment.getFloor());
            insertApartmentStatement.setLong(2, apartment.getApartmentClass().getId());
            insertApartmentStatement.setLong(3, apartment.getStatus().getId());
            insertApartmentStatement.setInt(4, apartment.getDemand());
            insertApartmentStatement.setBigDecimal(5, apartment.getPrice());
            insertApartmentStatement.setInt(6, apartment.getNumberOfPeople());
            insertApartmentStatement.setInt(7, apartment.getNumber());
            insertApartmentStatement.executeUpdate();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Updating apartment record failed during accessing database", e);
        }
    }

    @Override
    public void delete(final long number) throws DaoException {
        try (var deleteApartmentStatement = connection.prepareStatement(DELETE_APARTMENT_BY_NUMBER)) {
            deleteApartmentStatement.setLong(1, number);
            deleteApartmentStatement.executeUpdate();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Deleting apartment record by number failed during accessing database", e);
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    private Collection<Apartment> getSortedListOfApartments(final int skip,
                                                            final int count,
                                                            final String sortingSql) throws DaoException {
        try (var selectApartmentStatement = connection.prepareStatement(sortingSql)) {
            selectApartmentStatement.setInt(1, skip);
            selectApartmentStatement.setInt(2, count);
            return getApartmentsFromStatement(selectApartmentStatement);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Getting list of apartment records failed during accessing database", e);
        }
    }

    private Collection<Apartment> getApartmentsFromStatement(final PreparedStatement selectApartmentStatement) throws SQLException {
        final var resultSet = selectApartmentStatement.executeQuery();
        final var apartments = new ArrayList<Apartment>();
        while (resultSet.next()) {
            apartments.add(apartmentMapper.extractFromResultSet(resultSet));
        }
        return apartments;
    }
}
