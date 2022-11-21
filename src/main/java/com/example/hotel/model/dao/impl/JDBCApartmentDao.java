package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.DELETE_APARTMENT_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.INSERT_APARTMENT;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_BY_CLASS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_BY_FLOOR;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_BY_NUMBER_OF_PEOPLE;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_BY_STATUS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_CLASS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_CLASS_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_NUMBER_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_PRICE;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_PRICE_DESC;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENTS_SORTED_BY_STATUS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_APARTMENT_BY_NUMBER;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.SELECT_COUNT_APARTMENTS;
import static com.example.hotel.model.dao.sql.mysql.ApartmentSQL.UPDATE_APARTMENT;

public class JDBCApartmentDao implements ApartmentDao {
    private final Connection connection;
    private final EntityMapper<Apartment> apartmentMapper;

    public JDBCApartmentDao(Connection connection, EntityMapper<Apartment> apartmentMapper) {
        this.connection = connection;
        this.apartmentMapper = apartmentMapper;
    }

    @Override
    public Connection getConnection(){
        return connection;
    }

    @Override
    public List<Apartment> findByFloor(int floor, int skip, int count) throws SQLException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENTS_BY_FLOOR)) {
            selectApartmentStatement.setInt(1, floor);
            return getListOfApartments(skip, count, selectApartmentStatement);
        }
    }

    @Override
    public List<Apartment> findByClass(ApartmentClass apartmentClass, int skip, int count) throws SQLException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENTS_BY_CLASS)) {
            selectApartmentStatement.setInt(1, apartmentClass.getId());
            return getListOfApartments(skip, count, selectApartmentStatement);
        }
    }

    @Override
    public List<Apartment> findByStatus(ApartmentStatus apartmentStatus, int skip, int count) throws SQLException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENTS_BY_STATUS)) {
            selectApartmentStatement.setInt(1, apartmentStatus.getId());
            return getListOfApartments(skip, count, selectApartmentStatement);
        }
    }

    @Override
    public List<Apartment> findByNumberOfPeople(int numberOfPeople, int skip, int count) throws SQLException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENTS_BY_NUMBER_OF_PEOPLE)) {
            selectApartmentStatement.setInt(1, numberOfPeople);
            return getListOfApartments(skip, count, selectApartmentStatement);
        }
    }

    @Override
    public Optional<Apartment> findByNumber(long number) throws SQLException {
        return findById(number);
    }

    @Override
    public List<Apartment> findSortedByNumber(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_NUMBER);
    }

    @Override
    public List<Apartment> findSortedByNumberDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_NUMBER_DESC);
    }

    @Override
    public List<Apartment> findSortedByClass(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_CLASS);
    }

    @Override
    public List<Apartment> findSortedByClassDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_CLASS_DESC);
    }


    @Override
    public List<Apartment> findSortedByStatus(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_STATUS);
    }

    @Override
    public List<Apartment> findSortedByPrice(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_PRICE);
    }

    @Override
    public List<Apartment> findSortedByPriceDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_PRICE_DESC);
    }

    @Override
    public List<Apartment> findSortedByNumberOfPeople(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE);
    }

    @Override
    public List<Apartment> findSortedByNumberOfPeopleDescending(int skip, int count) throws SQLException {
        return getSortedListOfApartments(skip, count, SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE_DESC);
    }

    @Override
    public int getCount() throws SQLException {
        try (var statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_COUNT_APARTMENTS);
            return resultSet.getInt("count");
        }
    }


    @Override
    public Optional<Apartment> findById(long id) throws SQLException {
        try (var selectApartmentStatement = connection.prepareStatement(SELECT_APARTMENT_BY_NUMBER)) {
            selectApartmentStatement.setLong(1, id);
            var resultSet = selectApartmentStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(apartmentMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    private List<Apartment> getListOfApartments(int skip, int count, PreparedStatement selectApartmentStatement) throws SQLException {
        selectApartmentStatement.setInt(2, skip);
        selectApartmentStatement.setInt(3, count);
        var resultSet = selectApartmentStatement.executeQuery();
        var apartments = new ArrayList<Apartment>();
        while (resultSet.next()) {
            apartments.add(apartmentMapper.extractFromResultSet(resultSet));
        }
        return apartments;
    }

    private List<Apartment> getSortedListOfApartments(int skip, int count, String sortingSql) throws SQLException {
        try (var selectApartmentStatement = connection.prepareStatement(sortingSql)) {
            selectApartmentStatement.setInt(1, skip);
            selectApartmentStatement.setInt(2, count);
            var resultSet = selectApartmentStatement.executeQuery();
            var apartments = new ArrayList<Apartment>();
            while (resultSet.next()) {
                apartments.add(apartmentMapper.extractFromResultSet(resultSet));
            }
            return apartments;
        }
    }

    @Override
    public int create(Apartment apartment) throws SQLException {
        try (var insertApartmentStatement = connection.prepareStatement(INSERT_APARTMENT)) {
            insertApartmentStatement.setLong(1, apartment.getNumber());
            insertApartmentStatement.setLong(2, apartment.getFloor());
            insertApartmentStatement.setLong(3, apartment.getApartmentClass().getId());
            insertApartmentStatement.setLong(4, apartment.getApartmentStatus().getId());
            insertApartmentStatement.setInt(5, apartment.getDemand());
            insertApartmentStatement.setBigDecimal(6, apartment.getPrice());
            insertApartmentStatement.setInt(7, apartment.getNumberOfPeople());
            insertApartmentStatement.executeUpdate();
            return apartment.getNumber();
        }
    }

    @Override
    public void update(Apartment apartment) throws SQLException {
        try (var insertApartmentStatement = connection.prepareStatement(UPDATE_APARTMENT)) {
            insertApartmentStatement.setLong(1, apartment.getFloor());
            insertApartmentStatement.setLong(2, apartment.getApartmentClass().getId());
            insertApartmentStatement.setLong(3, apartment.getApartmentStatus().getId());
            insertApartmentStatement.setInt(4, apartment.getDemand());
            insertApartmentStatement.setBigDecimal(5, apartment.getPrice());
            insertApartmentStatement.setInt(6, apartment.getNumberOfPeople());
            insertApartmentStatement.setInt(7, apartment.getNumber());
            insertApartmentStatement.executeUpdate();
        }
    }

    @Override
    public void delete(long number) throws SQLException {
        try (var deleteApartmentStatement = connection.prepareStatement(DELETE_APARTMENT_BY_NUMBER)) {
            deleteApartmentStatement.setLong(1, number);
            deleteApartmentStatement.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
