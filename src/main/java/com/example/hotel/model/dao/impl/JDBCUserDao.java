package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.commons.Constants.ColumnLabels;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.Role;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static com.example.hotel.model.dao.commons.Tools.getGeneratedId;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.DELETE_PREVIOUS_ROLES;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.DELETE_USER_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.INSERT_UPDATED_ROLES;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.INSERT_USER;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.INSERT_USER_ROLE;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_COUNT_USERS;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_ROLES_BY_USER_ID;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_USERS_SORTED_BY_ID_LIMITED;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_USER_BY_ID;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.SELECT_USER_BY_LOGIN;
import static com.example.hotel.model.dao.commons.mysql.UserSQL.UPDATE_USER;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCUserDao implements UserDao {
    public final static Logger log = Logger.getLogger(JDBCUserDao.class);
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
    public int getCount() throws DaoException {
        try (var statement = connection.createStatement()) {
            final var resultSet = statement.executeQuery(SELECT_COUNT_USERS);
            resultSet.next();
            return resultSet.getInt(ColumnLabels.COUNT);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new DaoException("Counting user's records failed during accessing data from database", e);
        }
    }

    @Override
    public long create(final User user) throws DaoException {
        try (var insertUserStatement = connection.prepareStatement(INSERT_USER, RETURN_GENERATED_KEYS);
             var insertRoleStatement = connection.prepareStatement(INSERT_USER_ROLE)) {
            setStatementParametersForUser(user, insertUserStatement);
            insertUserStatement.executeUpdate();
            final var userId = getGeneratedId(insertUserStatement);

            for (final var role : user.getRoles()) {
                insertRoleStatement.setString(1, role.getName());
                insertRoleStatement.setLong(2, userId);
                insertRoleStatement.executeUpdate();
            }
            return userId;
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Creating new user record failed during accessing to database", e);
        }
    }

    @Override
    public Optional<User> findById(final long userId) throws DaoException {
        try (var selectUserStatement = connection.prepareStatement(SELECT_USER_BY_ID);
             var selectRolesStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)) {
            selectUserStatement.setLong(1, userId);
            final var resultSet = selectUserStatement.executeQuery();
            if (resultSet.next()) {
                final var user = userMapper.extractFromResultSet(resultSet);
                selectRolesStatement.setLong(1, userId);
                addRolesToUser(user, selectRolesStatement);
                return Optional.of(user);
            }
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Getting user's record by id failed during accessing data from database", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(final String login) throws DaoException {
        try (var selectUserStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
             var selectRolesStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)) {
            selectUserStatement.setString(1, login);
            final var resultSet = selectUserStatement.executeQuery();
            if (resultSet.next()) {
                final var user = userMapper.extractFromResultSet(resultSet);
                selectRolesStatement.setLong(1, user.getId());
                addRolesToUser(user, selectRolesStatement);
                return Optional.of(user);
            }
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Getting user's record by login failed during accessing data from database", e);
        }
        return Optional.empty();
    }

    @Override
    public Collection<User> findSortedById(final int skip, final int count) throws DaoException {
        try (var selectUserStatement = connection.prepareStatement(SELECT_USERS_SORTED_BY_ID_LIMITED);
             var selectRolesStatement = connection.prepareStatement(SELECT_ROLES_BY_USER_ID)) {
            selectUserStatement.setLong(1, skip);
            selectUserStatement.setLong(2, count);
            return getUsers(selectUserStatement, selectRolesStatement);
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            final var message = "Getting sorted list of user's records failed during accessing data from database";
            throw new DaoException(message, e);
        }
    }

    @Override
    public void update(final User user) throws DaoException {
        try (var updateUserStatement = connection.prepareStatement(UPDATE_USER);
             var deleteRolesStatement = connection.prepareStatement(DELETE_PREVIOUS_ROLES);
             var insertRolesStatement = connection.prepareStatement(INSERT_UPDATED_ROLES)) {
            setStatementParametersForUser(user, updateUserStatement);
            updateUserStatement.setBigDecimal(8, user.getMoney());
            updateUserStatement.setLong(9, user.getId());
            deleteRolesStatement.setLong(1, user.getId());
            updateUserStatement.executeUpdate();
            deleteRolesStatement.executeUpdate();

            final var roles = user.getRoles();
            if (roles == null) {
                return;
            }
            for (final var role : roles) {
                insertRolesStatement.setString(1, role.getName());
                insertRolesStatement.setLong(2, user.getId());
                insertRolesStatement.executeUpdate();
            }
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Updating user's record failed during accessing database", e);
        }
    }

    @Override
    public void delete(final long id) throws DaoException {
        try (var preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (final SQLException e){
            log.error(e.getMessage(), e);
            throw new DaoException("Deleting user record failed during accessing database", e);
        }
    }

    private void setStatementParametersForUser(final User user,
                                               final PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getFirstname());
        preparedStatement.setString(3, user.getLastname());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getPhone());
        preparedStatement.setInt(7, user.getStatus().getId());
    }

    private Collection<User> getUsers(final PreparedStatement selectUserStatement,
                                      final PreparedStatement selectRolesStatement) throws SQLException {
        final var usersSet = selectUserStatement.executeQuery();
        final var users = new ArrayList<User>();
        while (usersSet.next()) {
            var user = userMapper.extractFromResultSet(usersSet);
            selectRolesStatement.setLong(1, user.getId());
            addRolesToUser(user, selectRolesStatement);
            users.add(user);
        }
        return users;
    }

    private void addRolesToUser(final User user,
                                final PreparedStatement selectRolesStatement) throws SQLException {
        final var roles = new HashSet<Role>();
        final var rolesSet = selectRolesStatement.executeQuery();
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
