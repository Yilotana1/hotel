package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.commons.Constants;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.exception.UserNotFoundException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class UserServiceImpl implements UserService {


    private final DaoFactory daoFactory;
    public final static Logger log = Logger.getLogger(UserServiceImpl.class);

    public UserServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Optional<User> signIn(final String login, final String password) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(Constants.AUTO_COMMIT);
            final var user = userDao.findByLogin(login);
            if (user.isPresent() && user.get().getPassword().equals(password)) {
                userDao.getConnection().commit();
                return user;
            }
            return Optional.empty();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Sign in process has failed", e);
        }
    }

    @Override
    public Optional<User> getByLogin(final String login) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            return userDao.findByLogin(login);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Searching for user by login has failed", e);
        }
    }

    @Override
    public Collection<User> getUsers(final int skip, final int count) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            return userDao.findSortedById(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting users has failed", e);
        }
    }

    @Override
    public int count() throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            return userDao.getCount();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Counting of all users has failed", e);
        }
    }

    @Override
    public void updateMoneyAccount(final String login, final BigDecimal money) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(Constants.AUTO_COMMIT);
            final var user = userDao.findByLogin(login)
                    .orElseThrow(() -> new LoginIsNotFoundException("User with login = " + login + " not found"));
            user.updateMoneyAccount(money);
            userDao.update(user);
            userDao.getConnection().commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("User's money account couldn't get updated", e);
        }
    }

    @Override
    public User signUp(final UserDTO userDTO) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(Constants.AUTO_COMMIT);
            final var userFromDB = userDao.findByLogin(userDTO.getLogin());
            if (userFromDB.isPresent()) {
                throw new LoginIsNotUniqueException(
                        String.format("Login = %s is already present in the system", userDTO.getLogin()));
            }
            final var user = mapToUser(userDTO);
            user.assignAsClient();
            final var generatedId = userDao.create(user);
            final var createdUser = userDao.findById(generatedId).get();
            userDao.getConnection().commit();
            return createdUser;
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Sign up process has failed", e);
        }
    }

    @Override
    public void update(final UserDTO userDTO) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(Constants.AUTO_COMMIT);
            final var user = userDao
                    .findByLogin(userDTO.getLogin())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User with login = " + userDTO.getLogin() + " not found"));
            user.setLogin(userDTO.getLogin());
            user.setPhone(userDTO.getPhone());
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setPassword(userDTO.getPassword());
            user.setEmail(userDTO.getEmail());
            userDao.update(user);
            userDao.getConnection().commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException("User's update process has failed", e);
        }
    }

    @Override
    public void update(final long id, final UserStatus status, final Collection<Role> roles) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(Constants.AUTO_COMMIT);
            final var user = userDao.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id = " + id + " not found"));
            user.setStatus(status);
            user.setRoles(roles);
            userDao.update(user);
            userDao.getConnection().commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException("User's update process has failed", e);
        }
    }

    private User mapToUser(final UserDTO userDTO) {
        return User.builder()
                .login(userDTO.getLogin())
                .firstname(userDTO.getFirstname())
                .lastname(userDTO.getLastname())
                .phone(userDTO.getPhone())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
    }
}
