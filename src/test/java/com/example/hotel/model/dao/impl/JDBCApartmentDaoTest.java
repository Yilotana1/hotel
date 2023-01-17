package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.dao.mapper.ApartmentMapper;
import com.example.hotel.model.entity.Apartment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.hotel.model.dao.commons.mysql.ApartmentSQL.DELETE_APARTMENT_BY_NUMBER;
import static com.example.hotel.model.dao.commons.mysql.ApartmentSQL.INSERT_APARTMENT;
import static com.example.hotel.model.dao.commons.mysql.ApartmentSQL.SELECT_APARTMENT_BY_NUMBER;
import static com.example.hotel.model.dao.commons.mysql.ApartmentSQL.SELECT_COUNT_APARTMENTS;
import static com.example.hotel.model.dao.commons.mysql.ApartmentSQL.UPDATE_APARTMENT;
import static com.example.hotel.model.entity.enums.ApartmentClass.STANDARD;
import static com.example.hotel.model.entity.enums.ApartmentStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class JDBCApartmentDaoTest {
    private Connection connection;
    private ApartmentDao apartmentDao;
    private final Apartment expectedApartment = Apartment
            .builder()
            .number(1L)
            .apartmentStatus(FREE)
            .floor(1)
            .numberOfPeople(3)
            .price(new BigDecimal("312"))
            .demand(1)
            .apartmentClass(STANDARD)
            .build();

    @BeforeEach
    public void init() throws SQLException {
        connection = mock(Connection.class);
        final var apartmentMapper = mock(ApartmentMapper.class);
        when(apartmentMapper.extractFromResultSet(any(ResultSet.class))).thenReturn(expectedApartment);
        apartmentDao = new JDBCApartmentDao(
                connection,
                apartmentMapper);
    }

    @Test
    void getCount() throws SQLException {
        final var statement = mock(Statement.class);
        final var resultSet = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_COUNT_APARTMENTS)).thenReturn(resultSet);
        final var expectedCount = 1;
        when(resultSet.getInt(ColumnLabels.COUNT)).thenReturn(expectedCount);

        assertThat(apartmentDao.getCount()).isEqualTo(expectedCount);
        verify(resultSet, times(1)).next();

    }

    @Test
    void findById() throws SQLException {
        final var selectApartmentStatement = mock(PreparedStatement.class);
        final var resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(SELECT_APARTMENT_BY_NUMBER)).thenReturn(selectApartmentStatement);
        when(selectApartmentStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        final var apartment = apartmentDao.findById(expectedApartment.getNumber());
        assertThat(apartment).isPresent();
        assertThat(apartment.get().getNumber()).isEqualTo(expectedApartment.getNumber());
        assertThat(apartment.get().getFloor()).isEqualTo(expectedApartment.getFloor());
        assertThat(apartment.get().getPrice()).isEqualTo(expectedApartment.getPrice());
        assertThat(apartment.get().getDemand()).isEqualTo(expectedApartment.getDemand());
        assertThat(apartment.get().getStatus()).isEqualTo(expectedApartment.getStatus());
        assertThat(apartment.get().getApartmentClass()).isEqualTo(expectedApartment.getApartmentClass());
        verify(selectApartmentStatement, times(1)).setLong(anyInt(), anyLong());
    }

    @Test
    void create() throws SQLException {
        final var insertApartmentStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(INSERT_APARTMENT)).thenReturn(insertApartmentStatement);

        assertThat(apartmentDao.create(expectedApartment)).isEqualTo(expectedApartment.getNumber());
        verify(insertApartmentStatement, times(4)).setLong(anyInt(), anyLong());
        verify(insertApartmentStatement, times(2)).setInt(anyInt(), anyInt());
        verify(insertApartmentStatement, times(1)).setBigDecimal(anyInt(), any(BigDecimal.class));
        verify(insertApartmentStatement, times(1)).executeUpdate();
    }

    @Test
    void update() throws SQLException {
        final var updateApartmentStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(UPDATE_APARTMENT)).thenReturn(updateApartmentStatement);

        apartmentDao.update(expectedApartment);
        verify(updateApartmentStatement, times(4)).setLong(anyInt(), anyLong());
        verify(updateApartmentStatement, times(2)).setInt(anyInt(), anyInt());
        verify(updateApartmentStatement, times(1)).setBigDecimal(anyInt(), any(BigDecimal.class));
        verify(updateApartmentStatement, times(1)).executeUpdate();
    }

    @Test
    void delete() throws SQLException {
        final var deleteApartmentStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(DELETE_APARTMENT_BY_NUMBER)).thenReturn(deleteApartmentStatement);

        apartmentDao.delete(expectedApartment.getNumber());
        verify(deleteApartmentStatement, times(1)).setLong(anyInt(), anyLong());
        verify(deleteApartmentStatement, times(1)).executeUpdate();
    }

}