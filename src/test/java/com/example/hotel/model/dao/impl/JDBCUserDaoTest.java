package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.dao.commons.Tools;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.dao.mapper.RoleMapper;
import com.example.hotel.model.dao.mapper.UserMapper;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import static com.example.hotel.model.dao.commons.mysql.UserSQL.DELETE_PREVIOUS_ROLES;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.DELETE_USER_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.INSERT_UPDATED_ROLES;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.INSERT_USER;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.INSERT_USER_ROLE;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_ROLES_BY_USER_ID;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_USER_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.UPDATE_USER;
import static com.example.hotel.model.entity.enums.Role.ADMIN;
import static com.example.hotel.model.entity.enums.Role.CLIENT;
import static com.example.hotel.model.entity.enums.Role.MANAGER;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JDBCUserDaoTest {
    private Connection connection;
    private UserDao userDao;
    private final EntityMapper<User> userMapper = new UserMapper();
    private final EntityMapper<Role> rolesMapper = new RoleMapper();
    private final User expectedUser = User.builder()
            .id(1L)
            .money(new BigDecimal(30))
            .password("12345678")
            .status(NON_BLOCKED)
            .phone("31213123123")
            .firstname("john")
            .lastname("smith")
            .email("john@gmail.com")
            .login("john")
            .roles(Set.of(CLIENT, MANAGER, ADMIN))
            .build();

    @BeforeEach
    public void init() {
        connection = mock(Connection.class);
        userDao = new JDBCUserDao(connection, userMapper, rolesMapper);
    }

    @Test
    public void getCount_shouldReturnUserCount() throws SQLException {
        final var expectedCount = 3;
        final var statement = mock(Statement.class);
        final var resultSet = mock(ResultSet.class);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(connection.createStatement()).thenReturn(statement);
        when(resultSet.getInt(ColumnLabels.COUNT)).thenReturn(expectedCount);

        assertThat(userDao.getCount()).isEqualTo(expectedCount);
        verify(resultSet, times(1)).next();
    }

    @Test
    public void create_shouldCreateNewUser() throws SQLException {
        final var rolesLength = expectedUser.getRoles().size();
        final var insertUserStatement = mock(PreparedStatement.class);
        final var insertRoleStatement = mock(PreparedStatement.class);
        final var keys = mock(ResultSet.class);

        when(insertUserStatement.getGeneratedKeys()).thenReturn(keys);
        when(keys.getLong(Tools.ID_INDEX)).thenReturn(expectedUser.getId());
        when(connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS))
                .thenReturn(insertUserStatement);
        when(connection.prepareStatement(INSERT_USER_ROLE)).thenReturn(insertRoleStatement);

        assertThat(userDao.create(expectedUser)).isEqualTo(expectedUser.getId());
        verify(keys, times(1)).next();
        verify(insertUserStatement, times(1)).executeUpdate();
        verify(insertRoleStatement, times(rolesLength)).executeUpdate();
        verify(insertRoleStatement, times(rolesLength)).setString(anyInt(), anyString());
        verify(insertRoleStatement, times(rolesLength)).setLong(anyInt(), anyLong());
    }

    @Test
    public void givenUserWithEmptyRoles_update_shouldUpdateUser() throws SQLException {
        final var updateUserStatement = mock(PreparedStatement.class);
        final var deleteRolesStatement = mock(PreparedStatement.class);
        final var insertRoleStatement = mock(PreparedStatement.class);

        when(connection.prepareStatement(UPDATE_USER)).thenReturn(updateUserStatement);
        when(connection.prepareStatement(DELETE_PREVIOUS_ROLES)).thenReturn(deleteRolesStatement);
        when(connection.prepareStatement(INSERT_UPDATED_ROLES)).thenReturn(insertRoleStatement);

        final var user = User.builder().id(1L).build();
        userDao.update(user);

        verify(
                updateUserStatement,
                times(1)).setBigDecimal(anyInt(),
                any(BigDecimal.class));
        verify(updateUserStatement, times(1)).setLong(anyInt(), anyLong());
        verify(deleteRolesStatement, times(1)).setLong(anyInt(), anyLong());
        verify(updateUserStatement, times(1)).executeUpdate();
        verify(deleteRolesStatement, times(1)).executeUpdate();
    }

    @Test
    public void givenUser_update_shouldUpdateUser() throws SQLException {
        final var updateUserStatement = mock(PreparedStatement.class);
        final var deleteRolesStatement = mock(PreparedStatement.class);
        final var insertRoleStatement = mock(PreparedStatement.class);
        final var rolesSetLength = expectedUser.getRoles().size();

        when(connection.prepareStatement(UPDATE_USER)).thenReturn(updateUserStatement);
        when(connection.prepareStatement(DELETE_PREVIOUS_ROLES)).thenReturn(deleteRolesStatement);
        when(connection.prepareStatement(INSERT_UPDATED_ROLES)).thenReturn(insertRoleStatement);

        userDao.update(expectedUser);
        verify(
                updateUserStatement,
                times(1)).setBigDecimal(anyInt(),
                any(BigDecimal.class));
        verify(updateUserStatement, times(1)).setLong(anyInt(), anyLong());
        verify(deleteRolesStatement, times(1)).setLong(anyInt(), anyLong());
        verify(updateUserStatement, times(1)).executeUpdate();
        verify(deleteRolesStatement, times(1)).executeUpdate();
        verify(insertRoleStatement, times(rolesSetLength)).executeUpdate();
        verify(insertRoleStatement, times(rolesSetLength)).setString(anyInt(), anyString());
        verify(insertRoleStatement, times(rolesSetLength)).setLong(anyInt(), anyLong());
    }

    @Test
    public void findById_shouldReturnUser() throws SQLException {
        final var selectUserStatement = mock(PreparedStatement.class);
        final var selectRolesStatement = mock(PreparedStatement.class);
        final var resultSet = mock(ResultSet.class);
        final var rolesSet = mock(ResultSet.class);
        final var userMapper = mock(UserMapper.class);
        final var roleMapper = mock(RoleMapper.class);

        final var userDao = new JDBCUserDao(connection, userMapper, roleMapper);

        when(connection.prepareStatement(SELECT_USER_BY_ID)).thenReturn(selectUserStatement);
        when(connection.prepareStatement(SELECT_ROLES_BY_USER_ID)).thenReturn(selectRolesStatement);
        when(selectUserStatement.executeQuery()).thenReturn(resultSet);
        when(selectRolesStatement.executeQuery()).thenReturn(rolesSet);
        when(resultSet.next()).thenReturn(true);
        when(userMapper.extractFromResultSet(resultSet)).thenReturn(expectedUser);
        when(rolesSet.next()).thenReturn(false);

        final var user = userDao.findById(expectedUser.getId());
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getId()).isEqualTo(expectedUser.getId());
        assertThat(user.get().getLogin()).isEqualTo(expectedUser.getLogin());
        assertThat(user.get().getPassword()).isEqualTo(expectedUser.getPassword());
        assertThat(user.get().getMoney()).isEqualTo(expectedUser.getMoney());
        assertThat(user.get().getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(user.get().getFirstname()).isEqualTo(expectedUser.getFirstname());
        assertThat(user.get().getLastname()).isEqualTo(expectedUser.getLastname());
        assertThat(user.get().getPhone()).isEqualTo(expectedUser.getPhone());
        assertThat(user.get().getStatus()).isEqualTo(expectedUser.getStatus());

        verify(selectUserStatement, times(1)).setLong(anyInt(), anyLong());
        verify(selectUserStatement, times(1)).executeQuery();
        verify(selectRolesStatement, times(1)).setLong(anyInt(), anyLong());
    }

    @Test
    public void delete_shouldDeleteUser() throws SQLException {
        final var preparedStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(DELETE_USER_BY_ID)).thenReturn(preparedStatement);

        userDao.delete(anyLong());
        verify(preparedStatement, times(1)).setLong(anyInt(), anyLong());
        verify(preparedStatement, times(1)).executeUpdate();
    }
}