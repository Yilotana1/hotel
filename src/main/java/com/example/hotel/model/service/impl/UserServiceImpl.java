package com.example.hotel.model.service.impl;

import com.example.hotel.HelloServlet;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.exception.UserNotFoundException;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Optional;

import static com.example.hotel.model.entity.enums.Role.MANAGER;
import static com.example.hotel.model.service.exception.Messages.LOGIN_IS_NOT_UNIQUE;
import static com.example.hotel.model.service.exception.Messages.SERVICE_EXCEPTION;
import static com.example.hotel.model.service.exception.Messages.USER_WITH_SUCH_ID_NOT_FOUND;
import static com.example.hotel.model.service.exception.Messages.USER_WITH_SUCH_LOGIN_NOT_FOUND;

public class UserServiceImpl implements UserService {


    private final DaoFactory daoFactory = DaoFactory.getInstance();
    public final static Logger log = Logger.getLogger(HelloServlet.class);

    @Override
    public Optional<User> signIn(String login, String password) throws ServiceException {
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
    public User signUp(User user) throws ServiceException {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var userFromDB = userDao.findByLogin(user.getLogin());
            if (userFromDB.isPresent()) {
                throw new LoginIsNotUniqueException(String.format(LOGIN_IS_NOT_UNIQUE, user.getLogin()));
            }
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
    public void update(User user) {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var id = user.getId();
            userDao.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_SUCH_ID_NOT_FOUND, id)));

            userDao.update(user);
            userDao.getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public void block(String userLogin) {
        try (var userDao = daoFactory.createUserDao()) {
            userDao.getConnection().setAutoCommit(false);
            var user = userDao.findByLogin(userLogin)
                    .orElseThrow(() -> new UserNotFoundException(String.format(USER_WITH_SUCH_LOGIN_NOT_FOUND, userLogin)));

            user.getRoles().clear();
            userDao.update(user);
            userDao.getConnection().commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }


    @Override
    public void changeToManager(String userLogin) throws ServiceException {
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
    public void changeToClient(String userLogin) {
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
}
