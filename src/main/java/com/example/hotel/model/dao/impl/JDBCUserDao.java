package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.Tools;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.example.hotel.model.dao.sql.UserSQL.DELETE_USER_BY_ID_SQL;
import static com.example.hotel.model.dao.sql.UserSQL.INSERT_USER_SQL;
import static com.example.hotel.model.dao.sql.UserSQL.SELECT_COUNT_USERS;
import static com.example.hotel.model.dao.sql.UserSQL.SELECT_USER_BY_FULL_NAME_SQL;
import static com.example.hotel.model.dao.sql.UserSQL.SELECT_USER_BY_ID_SQL;
import static com.example.hotel.model.dao.sql.UserSQL.SELECT_USER_BY_LIMIT_SQL;
import static com.example.hotel.model.dao.sql.UserSQL.SELECT_USER_BY_LOGIN_SQL;
import static com.example.hotel.model.dao.sql.UserSQL.UPDATE_USER_SQL;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JDBCUserDao implements UserDao {

    private final Connection connection;
    private final EntityMapper<User> userMapper;

    public JDBCUserDao(Connection connection, EntityMapper<User> entityMapper) {
        this.connection = connection;
        this.userMapper = entityMapper;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public int getCount() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_COUNT_USERS);
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    @Override
    public int create(User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL, RETURN_GENERATED_KEYS)) {
            setStatementParameters(user, preparedStatement);
            preparedStatement.executeUpdate();
            return Tools.getGeneratedId(preparedStatement);
        }
    }

    @Override
    public Optional<User> findById(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(userMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByLimit(int start, int count) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LIMIT_SQL)) {
            preparedStatement.setLong(1, start - 1);
            preparedStatement.setLong(2, count);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(userMapper.extractFromResultSet(resultSet));
            }
            return users;
        }
    }

    @Override
    public Optional<User> findByLogin(String login) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN_SQL)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(userMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByFullName(String firstname, String lastname) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_FULL_NAME_SQL)) {
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(userMapper.extractFromResultSet(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public void update(User user) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
            setStatementParameters(user, preparedStatement);
            preparedStatement.setLong(7, user.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void delete(long id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    private void setStatementParameters(User user, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getFirstname());
        preparedStatement.setString(3, user.getLastname());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getPhone());
    }

    @Override
    public void close() throws SQLException {
//        connection.close();
    }
}
