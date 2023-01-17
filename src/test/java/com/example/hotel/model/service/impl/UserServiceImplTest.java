package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.commons.Constants;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static com.example.hotel.model.entity.enums.Role.CLIENT;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserServiceImplTest {

    private UserDao userDao;
    private UserService userService;

    private User testUser;
    private final UserDTO userDTO = UserDTO.builder()
            .password("12345678")
            .phone("31213123123")
            .firstname("john")
            .lastname("smith")
            .email("john@gmail.com")
            .login("john")
            .build();
    private Connection connection;


    @BeforeEach
    public void init() {
        final var daoFactory = mock(DaoFactory.class);
        userDao = mock(UserDao.class);
        when(daoFactory.createUserDao()).thenReturn(userDao);
        connection = mock(Connection.class);
        when(userDao.getConnection()).thenReturn(connection);
        userService = new UserServiceImpl(daoFactory);

        testUser = User.builder()
                .id(1L)
                .money(new BigDecimal(30))
                .password("12345678")
                .status(NON_BLOCKED)
                .phone("31213123123")
                .firstname("john")
                .lastname("smith")
                .email("john@gmail.com")
                .login("john")
                .roles(Set.of(CLIENT))
                .build();
    }

    @Test
    void givenLoginAndPassword_signIn_shouldReturnSignedInUser() throws SQLException {
        //when
        when(userDao.findByLogin(anyString())).thenReturn(Optional.of(testUser));

        //then
        final var optionalUser = userService.signIn(testUser.getLogin(), testUser.getPassword());

        assertThat(optionalUser).isPresent();
        final var user = optionalUser.get();
        assertThat(user.getId()).isEqualTo(testUser.getId());
        assertThat(user.getFirstname()).isEqualTo(testUser.getFirstname());
        assertThat(user.getLastname()).isEqualTo(testUser.getLastname());
        assertThat(user.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(user.getPhone()).isEqualTo(testUser.getPhone());
        assertThat(user.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(user.getStatus()).isEqualTo(testUser.getStatus());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());

        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();

    }

    @Test
    void givenWrongLoginOrPassword_signIn_shouldReturnEmptyOptional() {
        //when
        when(userDao.findByLogin(anyString())).thenReturn(Optional.empty());

        //then
        final var optionalUser = userService.signIn(testUser.getLogin(), testUser.getPassword());
        assertThat(optionalUser).isNotPresent();
    }

    @Test
    void givenMoneyToAdd_updateMoneyAccount_shouldUpdateCurrentMoneyBalanceForClient() throws SQLException {
        //given
        final var moneyToAdd = new BigDecimal("123");
        final var currentBalance = testUser.getMoney();
        final var expectedBalance = currentBalance.add(moneyToAdd);
        //when
        when(userDao.findByLogin(anyString())).thenReturn(Optional.of(testUser));

        //then
        userService.updateMoneyAccount(testUser.getLogin(), moneyToAdd);
        assertThat(testUser.getMoney()).isEqualTo(expectedBalance);

        verify(userDao, times(1)).update(any(User.class));
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void givenMoneyToAdd_updateMoneyAccount_shouldThrowExceptionBecauseUserWithSpecifiedLoginNotFound() {
        //given
        final var moneyToAdd = new BigDecimal("123");

        //when
        when(userDao.findByLogin(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(LoginIsNotFoundException.class,
                () -> userService.updateMoneyAccount(testUser.getLogin(), moneyToAdd));
    }

    @Test
    void givenUserDTO_signUp_shouldReturnSignedUpUser() throws SQLException {
        //when
        when(userDao.findByLogin(anyString())).thenReturn(Optional.empty());
        when(userDao.create(any(User.class))).thenReturn(testUser.getId());
        when(userDao.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        //then
        final var user = userService.signUp(userDTO);

        assertThat(user.getId()).isEqualTo(testUser.getId());
        assertThat(user.getRoles().contains(CLIENT)).isTrue();
        assertThat(user.getRoles().size() == 1).isTrue();
        assertThat(user.getStatus()).isEqualTo(NON_BLOCKED);
        assertThat(user.getMoney()).isEqualTo(testUser.getMoney());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPhone()).isEqualTo(testUser.getPhone());
        assertThat(user.getFirstname()).isEqualTo(testUser.getFirstname());
        assertThat(user.getLastname()).isEqualTo(testUser.getLastname());
        assertThat(user.getLogin()).isEqualTo(testUser.getLogin());
        assertThat(user.getPassword()).isEqualTo(testUser.getPassword());
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void givenUserDTO_signUp_shouldThrowLoginInNotUniqueException() {
        //when
        when(userDao.findByLogin(anyString())).thenReturn(Optional.of(testUser));

        //then
        assertThrows(LoginIsNotUniqueException.class, () -> userService.signUp(userDTO));
    }

}