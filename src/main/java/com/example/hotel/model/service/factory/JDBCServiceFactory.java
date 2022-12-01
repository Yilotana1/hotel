package com.example.hotel.model.service.factory;

import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.impl.UserServiceImpl;

public class JDBCServiceFactory extends ServiceFactory {

    @Override
    public UserService createUserService() {
        return new UserServiceImpl();
    }


}
