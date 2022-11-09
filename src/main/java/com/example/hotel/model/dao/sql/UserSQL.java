package com.example.hotel.model.dao.sql;

public interface UserSQL {
    String SELECT_COUNT_USERS = "SELECT COUNT(id) AS count FROM user";
    String INSERT_USER_SQL = "INSERT INTO user (login, firstname, lastname, email, password, phone)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    String SELECT_USER_BY_ID_SQL = "SELECT * FROM user WHERE id = ?";
    String SELECT_USER_BY_LIMIT_SQL = "SELECT * FROM user LIMIT ?, ?";
    String SELECT_USER_BY_LOGIN_SQL = "SELECT * FROM user WHERE login = ?";
    String SELECT_USER_BY_FULL_NAME_SQL = "SELECT * FROM user WHERE firstname = ? and lastname = ?";
    String UPDATE_USER_SQL = "UPDATE user SET login = ?, firstname = ?, lastname = ?, email = ?," +
            "phone = ?, password = ? WHERE id = ?";
    String DELETE_USER_BY_ID_SQL = "DELETE FROM user WHERE id = ?";

}
