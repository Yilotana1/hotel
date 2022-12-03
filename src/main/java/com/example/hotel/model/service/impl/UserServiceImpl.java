package com.example.hotel.model.service.impl;

import com.example.hotel.controller.Servlet;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.exception.UserNotFoundException;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;

import static com.example.hotel.model.entity.enums.Role.CLIENT;
import static com.example.hotel.model.entity.enums.Role.MANAGER;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;
import static com.example.hotel.model.service.exception.Messages.LOGIN_IS_NOT_UNIQUE;
import static com.example.hotel.model.service.exception.Messages.SERVICE_EXCEPTION;
import static com.example.hotel.model.service.exception.Messages.USER_WITH_SUCH_LOGIN_NOT_FOUND;

public class UserServiceImpl implements UserService {


    private final DaoFactory daoFactory;
    public final static Logger log = Logger.getLogger(Servlet.class);

    public UserServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Optional<User> signIn(final String login, final String password) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var user = userDao.findByLogin(login);
            if (user.isPresent() && user.get().getPassword().equals(password)) {
                userDao.getConnection().commit();
                return user;
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public Optional<User> getByLogin(final String login) {
        try (var userDao = daoFactory.createUserDao()) {
            return userDao.findByLogin(login);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public User signUp(final UserDTO userDTO) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var userFromDB = userDao.findByLogin(userDTO.getLogin());
            if (userFromDB.isPresent()) {
                throw new LoginIsNotUniqueException(String.format(LOGIN_IS_NOT_UNIQUE, userDTO.getLogin()));
            }
            var user = mapToUser(userDTO);
            user.getRoles().add(CLIENT);
            user.setStatus(NON_BLOCKED);
            var generatedId = userDao.create(user);
            var createdUser = userDao.findById(generatedId).get();
            userDao.getConnection().commit();
            return createdUser;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public void update(final UserDTO userDTO) {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            final var user = userDao.findByLogin(userDTO.getLogin())
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_SUCH_LOGIN_NOT_FOUND, userDTO.getLogin())));
            user.setLogin(userDTO.getLogin());
            user.setPhone(userDTO.getPhone());
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setPassword(userDTO.getPassword());
            user.setEmail(userDTO.getEmail());
            userDao.update(user);
            userDao.getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public void block(final String userLogin) {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var user = userDao.findByLogin(userLogin)
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_SUCH_LOGIN_NOT_FOUND, userLogin)));

            user.block();
            userDao.update(user);
            userDao.getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }


    @Override
    public void changeToManager(final String userLogin) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var client = userDao
                    .findByLogin(userLogin)
                    .orElseThrow(() -> new LoginIsNotFoundException(USER_WITH_SUCH_LOGIN_NOT_FOUND));

            var roles = client.getRoles();
            if (!roles.contains(MANAGER)) {
                roles.add(MANAGER);
            }
            userDao.update(client);
            userDao.getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public void changeToClient(final String userLogin) {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var client = userDao
                    .findByLogin(userLogin)
                    .orElseThrow(() -> new LoginIsNotFoundException(USER_WITH_SUCH_LOGIN_NOT_FOUND));

            var roles = client.getRoles();
            roles.remove(MANAGER);
            userDao.update(client);
            userDao.getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
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
                .roles(new HashSet<>())
                .build();
    }
}
