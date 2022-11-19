package com.example.hotel.model.dao;

import com.example.hotel.model.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDao extends GenericDao<User>{
    Optional<User> findByLogin(String login) throws SQLException;
    List<User> findByFullName(String firstname, String lastname) throws SQLException;
}
