package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.dao.mapper.TemporaryApplicationMapper;
import com.example.hotel.model.entity.TemporaryApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static com.example.hotel.model.dao.commons.Tools.ID_INDEX;
import static com.example.hotel.model.dao.commons.mysql.TemporaryApplicationSQL.DELETE_TEMPORARY_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.TemporaryApplicationSQL.INSERT_TEMPORARY_APPLICATION;
import static com.example.hotel.model.dao.commons.mysql.TemporaryApplicationSQL.SELECT_COUNT_TEMPORARY_APPLICATION;
import static com.example.hotel.model.dao.commons.mysql.TemporaryApplicationSQL.SELECT_TEMPORARY_APPLICATION_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.TemporaryApplicationSQL.UPDATE_APARTMENT_REQUEST;
import static com.example.hotel.model.entity.enums.ApartmentClass.SUITE;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class JDBCTemporaryApplicationDaoTest {

    private Connection connection;
    private TemporaryApplicationDao temporaryApplicationDao;
    private TemporaryApplicationMapper temporaryApplicationMapper;
    private final TemporaryApplication expectedTemporaryApplication =
            TemporaryApplication
                    .builder()
                    .id(1L)
                    .clientLogin("login")
                    .creationDate(LocalDate.now())
                    .stayLength(3)
                    .numberOfPeople(3)
                    .apartmentClass(SUITE)
                    .build();

    @BeforeEach
    public void init() {
        connection = mock(Connection.class);
        temporaryApplicationMapper = mock(TemporaryApplicationMapper.class);
        temporaryApplicationDao = new JDBCTemporaryApplicationDao(
                connection,
                temporaryApplicationMapper);
    }

    @Test
    void getCount_shouldReturnCountOfTemporaryApplications() throws SQLException {
        final var statement = mock(Statement.class);
        final var resultSet = mock(ResultSet.class);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(SELECT_COUNT_TEMPORARY_APPLICATION)).thenReturn(resultSet);
        final var expectedCount = 1;
        when(resultSet.getInt(ColumnLabels.COUNT)).thenReturn(expectedCount);

        assertThat(temporaryApplicationDao.getCount()).isEqualTo(expectedCount);
        verify(resultSet, times(1)).next();
    }

    @Test
    void create_shouldReturnNewTemporaryApplication() throws SQLException {
        final var insertTemporaryApplicationStatement = mock(PreparedStatement.class);
        final var keys = mock(ResultSet.class);
        when(connection.prepareStatement(INSERT_TEMPORARY_APPLICATION, RETURN_GENERATED_KEYS))
                .thenReturn(insertTemporaryApplicationStatement);
        when(insertTemporaryApplicationStatement.getGeneratedKeys()).thenReturn(keys);
        when(keys.getLong(ID_INDEX)).thenReturn(expectedTemporaryApplication.getId());

        final var id = expectedTemporaryApplication.getId();
        assertThat(temporaryApplicationDao.create(expectedTemporaryApplication)).isEqualTo(id);
        verify(insertTemporaryApplicationStatement, times(1)).setLong(anyInt(), anyLong());
        verify(insertTemporaryApplicationStatement, times(2)).setInt(anyInt(), anyInt());
        verify(insertTemporaryApplicationStatement, times(1)).setString(anyInt(), anyString());
        verify(insertTemporaryApplicationStatement, times(1)).setDate(anyInt(), any(Date.class));
        verify(keys).next();
        verify(insertTemporaryApplicationStatement, times(1)).executeUpdate();
    }

    @Test
    void findById_shouldReturnTemporaryApplication() throws SQLException {
        final var selectTemporaryApplicationStatement = mock(PreparedStatement.class);
        final var resultSet = mock(ResultSet.class);

        when(connection
                .prepareStatement(SELECT_TEMPORARY_APPLICATION_BY_ID))
                .thenReturn(selectTemporaryApplicationStatement);
        when(selectTemporaryApplicationStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(temporaryApplicationMapper.extractFromResultSet(resultSet)).thenReturn(expectedTemporaryApplication);

        final var temporaryApplication = temporaryApplicationDao.findById(expectedTemporaryApplication.getId());
        assertThat(temporaryApplication.isPresent()).isTrue();
        assertThat(temporaryApplication.get().getId()).isEqualTo(expectedTemporaryApplication.getId());
        assertThat(temporaryApplication.get().getNumberOfPeople()).isEqualTo(expectedTemporaryApplication.getNumberOfPeople());
        assertThat(temporaryApplication.get().getStayLength()).isEqualTo(expectedTemporaryApplication.getStayLength());
        assertThat(temporaryApplication.get().getApartmentClass()).isEqualTo(expectedTemporaryApplication.getApartmentClass());
        assertThat(temporaryApplication.get().getClientLogin()).isEqualTo(expectedTemporaryApplication.getClientLogin());
        assertThat(temporaryApplication.get().getCreationDate()).isEqualTo(expectedTemporaryApplication.getCreationDate());
    }

    @Test
    void update_shouldUpdateTemporaryApplication() throws SQLException {
        final var insertTemporaryApplicationStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(UPDATE_APARTMENT_REQUEST))
                .thenReturn(insertTemporaryApplicationStatement);

        temporaryApplicationDao.update(expectedTemporaryApplication);
        verify(insertTemporaryApplicationStatement, times(3)).setInt(anyInt(), anyInt());
        verify(insertTemporaryApplicationStatement, times(1)).setLong(anyInt(), anyLong());
        verify(insertTemporaryApplicationStatement, times(1)).executeUpdate();
    }

    @Test
    void delete_shouldDeleteApplicationById() throws SQLException {
        final var deleteTemporaryApplicationStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(DELETE_TEMPORARY_APPLICATION_BY_ID))
                .thenReturn(deleteTemporaryApplicationStatement);

        temporaryApplicationDao.delete(anyLong());
        verify(deleteTemporaryApplicationStatement, times(1)).setLong(anyInt(), anyLong());
        verify(deleteTemporaryApplicationStatement, times(1)).executeUpdate();
    }

}