package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.example.hotel.model.dao.Tools.getGeneratedId;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.DELETE_PREVIOUS_ROLES;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.DELETE_USER_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.INSERT_UPDATED_ROLES;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.INSERT_USER;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.INSERT_USER_ROLE;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.SELECT_COUNT_USERS;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.SELECT_ROLES_BY_USER_ID;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.SELECT_USERS_SORTED_BY_ID_LIMITED;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.SELECT_USER_BY_ID;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.SELECT_USER_BY_LOGIN;
import static com.example.hotel.model.dao.sql.mysql.UserSQL.UPDATE_USER;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCUserDao implements UserDao {

    private final Connection connection;
    private final EntityMapper<User> userMapper;
    private final EntityMapper<Role> roleMapper;

    public JDBCUserDao(Connection connection, EntityMapper<User> userMapper, EntityMapper<Role> roleMapper) {
        this.connection = connection;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public int getCount() throws SQLException {
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(SELECT_COUNT_USERS);
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    @Override
    public int create(User user) throws SQLException {
        try (var insertUserStatement = connection.prepareStatement(INSERT_USER, RETURN_GENERATED_KEYS);
             var insertRoleStatement = connection.prepareStatement(INSERT_USER_ROLE)) {
            setStatementParametersForUser(user, insertUserStatement);
            insertUserStatement.executeUpdate();
            var userId = getGeneratedId(insertUserStatement);

            for (var role : user.getRoles()) {
                insertRoleStatement.setString(1, role.getName());
                insertRoleStatement.setLong(2, userId);
                insertRoleStatement.executeUpdate();
            }
            return userId;
        }
    }

    @Override
    public Optional<User> findById(long userId) throws SQLException {
        try (var selectUserStatement = connection.prepareStatement(SELECT_USER_BY_ID);
             var selectRolesStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)) {
            selectUserStatement.setLong(1, userId);
            var usersSet = selectUserStatement.executeQuery();
            if (usersSet.next()) {
                var user = userMapper.extractFromResultSet(usersSet);
                selectRolesStatement.setLong(1, userId);
                addRolesToUser(user, selectRolesStatement);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) throws SQLException {
        try (var selectUserStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
             var selectRolesStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)) {
            selectUserStatement.setString(1, login);
            var usersSet = selectUserStatement.executeQuery();
            if (usersSet.next()) {
                var user = userMapper.extractFromResultSet(usersSet);
                selectRolesStatement.setLong(1, user.getId());
                addRolesToUser(user, selectRolesStatement);
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findSortedById(int skip, int count) throws SQLException {
        try (var selectUserStatement = connection.prepareStatement(SELECT_USERS_SORTED_BY_ID_LIMITED);
             var selectRolesStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)) {
            selectUserStatement.setLong(1, skip);
            selectUserStatement.setLong(2, count);
            return getUsers(selectUserStatement, selectRolesStatement);
        }
    }
    @Override
    public void update(User user) throws SQLException {
        try (var updateUserStatement = connection.prepareStatement(UPDATE_USER);
             var deleteRolesStatement = connection.prepareStatement(DELETE_PREVIOUS_ROLES);
             var insertRolesStatement = connection.prepareStatement(INSERT_UPDATED_ROLES)) {
            setStatementParametersForUser(user, updateUserStatement);
            updateUserStatement.setBigDecimal(8, user.getMoney());
            updateUserStatement.setLong(9, user.getId());
            deleteRolesStatement.setLong(1, user.getId());
            updateUserStatement.executeUpdate();
            deleteRolesStatement.executeUpdate();

            var roles = user.getRoles();
            if (roles == null) {
                return;
            }
            for (var role : roles) {
                insertRolesStatement.setString(1, role.getName());
                insertRolesStatement.setLong(2, user.getId());
                insertRolesStatement.executeUpdate();
            }
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        try (var preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private void setStatementParametersForUser(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getFirstname());
        preparedStatement.setString(3, user.getLastname());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getPhone());
        preparedStatement.setInt(7, user.getStatus().getId());
    }

    private ArrayList<User> getUsers(PreparedStatement selectUserStatement, PreparedStatement selectRolesStatement) throws SQLException {
        var usersSet = selectUserStatement.executeQuery();
        var users = new ArrayList<User>();
        while (usersSet.next()) {
            var user = userMapper.extractFromResultSet(usersSet);
            selectRolesStatement.setLong(1, user.getId());
            addRolesToUser(user, selectRolesStatement);
            users.add(user);
        }
        return users;
    }

    private void addRolesToUser(User user, PreparedStatement selectRolesStatement) throws SQLException {
        var roles = new HashSet<Role>();
        var rolesSet = selectRolesStatement.executeQuery();
        while (rolesSet.next()) {
            roles.add(roleMapper.extractFromResultSet(rolesSet));
        }
        user.setRoles(roles);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
