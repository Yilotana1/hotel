package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.dao.mapper.ApplicationMapper;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.hotel.model.dao.commons.Tools.ID_INDEX;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.DELETE_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.INSERT_APPLICATION;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.SELECT_COUNT_APPLICATIONS;
import static com.example.hotel.model.dao.commons.mysql.ApplicationSQL.UPDATE_APPLICATION;
import static com.example.hotel.model.entity.enums.ApplicationStatus.NOT_APPROVED;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class JDBCApplicationDaoTest {


    private Connection connection;
    private ApplicationDao applicationDao;
    private final Application expectedApplication = Application
            .builder()
            .id(1L)
            .stayLength(3)
            .price(new BigDecimal("357"))
            .startDate(LocalDate.MAX)
            .endDate(LocalDate.MAX)
            .creationDate(LocalDateTime.MAX)
            .lastModified(LocalDateTime.MAX)
            .client(
                    User
                            .builder()
                            .id(1L)
                            .build())
            .apartment(
                    Apartment
                            .builder()
                            .number(3L)
                            .build()
            )
            .applicationStatus(NOT_APPROVED)
            .build();

    @BeforeEach
    public void init() throws SQLException {
        connection = mock(Connection.class);
        final var applicationMapper = mock(ApplicationMapper.class);
        when(applicationMapper.extractFromResultSet(any(ResultSet.class))).thenReturn(expectedApplication);
        applicationDao = new JDBCApplicationDao(
                connection,
                applicationMapper);
    }

    @Test
    void getCount_shouldReturnCountOfApplications() throws SQLException {
        final var countStatement = mock(Statement.class);
        final var resultSet = mock(ResultSet.class);
        final var expectedCount = 1;

        when(connection.createStatement()).thenReturn(countStatement);
        when(countStatement.executeQuery(SELECT_COUNT_APPLICATIONS)).thenReturn(resultSet);
        when(resultSet.getInt(ColumnLabels.COUNT)).thenReturn(expectedCount);

        assertThat(applicationDao.getCount()).isEqualTo(expectedCount);
        verify(resultSet, times(1)).next();
    }

    @Test
    void create_shouldCreateNewApplication() throws SQLException {
        final var createApplicationStatement = mock(PreparedStatement.class);
        final var keys = mock(ResultSet.class);

        when(connection.prepareStatement(INSERT_APPLICATION, RETURN_GENERATED_KEYS))
                .thenReturn(createApplicationStatement);
        when(createApplicationStatement.getGeneratedKeys()).thenReturn(keys);
        when(keys.getLong(ID_INDEX)).thenReturn(expectedApplication.getId());

        assertThat(applicationDao.create(expectedApplication)).isEqualTo(expectedApplication.getId());
        verify(createApplicationStatement, times(3)).setLong(anyInt(), anyLong());
        verify(createApplicationStatement, times(1)).setBigDecimal(anyInt(), any(BigDecimal.class));
        verify(createApplicationStatement, times(2)).setTimestamp(anyInt(), any(Timestamp.class));
        verify(createApplicationStatement, times(2)).setDate(anyInt(), any(Date.class));
        verify(createApplicationStatement, times(1)).setInt(anyInt(), anyInt());
        verify(keys, times(1)).next();
        verify(createApplicationStatement, times(1)).executeUpdate();
    }

    @Test
    void update() throws SQLException {
        final var updateApplicationStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(UPDATE_APPLICATION)).thenReturn(updateApplicationStatement);

        applicationDao.update(expectedApplication);
        verify(updateApplicationStatement, times(4)).setLong(anyInt(), anyLong());
        verify(updateApplicationStatement, times(1)).setBigDecimal(anyInt(), any(BigDecimal.class));
        verify(updateApplicationStatement, times(1)).setTimestamp(anyInt(), any(Timestamp.class));
        verify(updateApplicationStatement, times(2)).setDate(anyInt(), any(Date.class));
        verify(updateApplicationStatement, times(1)).setInt(anyInt(), anyInt());
        verify(updateApplicationStatement, times(1)).executeUpdate();
    }

    @Test
    void delete_shouldDeleteApplicationById() throws SQLException {
        final var deleteApplicationStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(DELETE_APPLICATION_BY_ID)).thenReturn(deleteApplicationStatement);

        applicationDao.delete(anyLong());
        verify(deleteApplicationStatement).setLong(anyInt(), anyLong());
        verify(deleteApplicationStatement).executeUpdate();
    }

    @Test
    void givenApplicationId_findById_shouldReturnApplication() throws SQLException {
        final var selectApplicationStatement = mock(PreparedStatement.class);
        final var resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(SELECT_APPLICATION_BY_ID)).thenReturn(selectApplicationStatement);
        when(selectApplicationStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        final var application = applicationDao.findById(expectedApplication.getId());
        assertThat(application).isPresent();
        assertThat(application.get().getId()).isEqualTo(expectedApplication.getId());
        assertThat(application.get().getStayLength()).isEqualTo(expectedApplication.getStayLength());
        assertThat(application.get().getClientLogin()).isEqualTo(expectedApplication.getClientLogin());
        assertThat(application.get().getPrice()).isEqualTo(expectedApplication.getPrice());
        assertThat(application.get().getCreationDate()).isEqualTo(expectedApplication.getCreationDate());
        assertThat(application.get().getLastModified()).isEqualTo(expectedApplication.getLastModified());
        assertThat(application.get().getStartDate()).isEqualTo(expectedApplication.getStartDate());
        assertThat(application.get().getEndDate()).isEqualTo(expectedApplication.getEndDate());
        assertThat(application.get().getStatus()).isEqualTo(expectedApplication.getStatus());
        verify(selectApplicationStatement, times(1)).setLong(anyInt(), anyLong());
    }
}