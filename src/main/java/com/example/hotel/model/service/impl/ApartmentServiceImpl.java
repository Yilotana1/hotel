package com.example.hotel.model.service.impl;

import com.example.hotel.controller.Servlet;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static com.example.hotel.model.service.exception.Messages.SERVICE_EXCEPTION;

public class ApartmentServiceImpl implements ApartmentService {
    public final static Logger log = Logger.getLogger(Servlet.class);

    private final DaoFactory daoFactory;

    public ApartmentServiceImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Apartment> getApartmentsSortedByPrice(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            final var apartments = apartmentDao.findSortedByPrice(skip, count);
            apartmentDao.getConnection().close();
            return apartments;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public List<Apartment> getApartmentsSortedByPeople(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByNumberOfPeople(skip, count);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public List<Apartment> getApartmentsSortedByClass(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByClass(skip, count);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public List<Apartment> getApartmentsSortedByStatus(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByStatus(skip, count);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    public int count() {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.getCount();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }
}
