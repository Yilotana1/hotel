package com.example.hotel.model.dao.sql.mysql;

public interface UserSQL {
    String SELECT_COUNT_USERS = "SELECT COUNT(id) AS count FROM user";
    String INSERT_USER = "INSERT INTO user (login, firstname, lastname, email, password, phone, status_id)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    String SELECT_USER_BY_ID = "SELECT * FROM user WHERE id = ?";
    String SELECT_ROLES_BY_USER_ID = "SELECT * FROM role WHERE user_id = ?";
    String SELECT_USERS_SORTED_BY_ID_LIMITED = "SELECT * FROM user LIMIT ?, ?";
    String SELECT_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    String SELECT_USERS_BY_FULL_NAME = "SELECT * FROM user WHERE firstname = ? AND lastname = ? LIMIT ?, ?";
    String UPDATE_USER = "UPDATE user SET login = ?, firstname = ?, lastname = ?, email = ?," +
            "password = ?, phone = ?, status_id = ?, money = ? WHERE id = ?";
    String DELETE_PREVIOUS_ROLES = "DELETE FROM role WHERE user_id = ?";
    String INSERT_UPDATED_ROLES = "INSERT INTO role (role, user_id) VALUES (?, ?);";
    String DELETE_USER_BY_ID = "DELETE FROM user WHERE id = ?";
    String INSERT_USER_ROLE = "INSERT INTO role (role, user_id) VALUES (?, ?)";

}
