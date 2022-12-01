package com.example.hotel.model.dao.impl;

import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.mapper.EntityMapper;
import com.example.hotel.model.dao.mapper.RoleMapper;
import com.example.hotel.model.dao.mapper.UserMapper;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.mockito.Mockito.mock;

class JDBCUserDaoTest {


    private Connection connection;
    private UserDao userDao;
    private final EntityMapper<User> userMapper = new UserMapper();
    private final EntityMapper<Role> rolesMapper = new RoleMapper();

    @BeforeEach
    void init(){
        connection = mock(Connection.class);
        userDao = new JDBCUserDao(connection, userMapper, rolesMapper);
    }

    @Test
    void getCount_shouldReturnUserCount() {
        
    }

    @Test
    void create() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByLimit() {
    }

    @Test
    void findByLogin() {
    }

    @Test
    void findByFullName() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}