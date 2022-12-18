package com.example.hotel.model.service;

import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import com.example.hotel.model.service.exception.ServiceException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> signIn(String login, String password) throws ServiceException;

    Optional<User> getByLogin(String login) throws ServiceException;

    Collection<User> getUsers(int skip, int count) throws ServiceException;

    int count() throws ServiceException;
    void updateMoneyAccount(String login, BigDecimal money) throws ServiceException;

    User signUp(UserDTO userDTO) throws ServiceException;

    void update(UserDTO userDTO) throws ServiceException;
    void update(long id, UserStatus status, Collection<Role> roles) throws ServiceException;
}
