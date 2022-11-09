package com.example.hotel.model.dao;

import com.example.hotel.model.entity.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao extends GenericDao<User>{
    Optional<User> findByLogin(String login) throws SQLException;
    Optional<User> findByFullName(String firstname, String lastname) throws SQLException;
}
