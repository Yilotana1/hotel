package com.example.hotel.model.service.factory;

import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.impl.ApartmentServiceImpl;
import com.example.hotel.model.service.impl.ApplicationServiceImpl;
import com.example.hotel.model.service.impl.UserServiceImpl;

public class JDBCServiceFactory extends ServiceFactory {


    private final DaoFactory daoFactory = DaoFactory.getInstance();

    @Override
    public UserService createUserService() {
        return new UserServiceImpl(daoFactory);
    }

    @Override
    public ApplicationService createApplicationService() {
        return new ApplicationServiceImpl(daoFactory);
    }

    @Override
    public ApartmentService createApartmentService() {
        return new ApartmentServiceImpl(daoFactory);
    }
}
