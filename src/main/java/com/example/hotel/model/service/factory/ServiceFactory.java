package com.example.hotel.model.service.factory;

import com.example.hotel.model.service.UserService;

public abstract class ServiceFactory {
    private static ServiceFactory serviceFactory;

    public abstract UserService createUserService();


    public static ServiceFactory getInstance() {
        if (serviceFactory == null) {
            synchronized (ServiceFactory.class) {
                if (serviceFactory == null) {
                    ServiceFactory temp = new JDBCServiceFactory();
                    serviceFactory = temp;
                }
            }
        }
        return serviceFactory;
    }

}
