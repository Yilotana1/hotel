package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao extends GenericDao<User>{
    Collection<User> findSortedById(int skip, int count) throws DaoException;
    Optional<User> findByLogin(String login) throws DaoException;
}