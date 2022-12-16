package com.example.hotel.model.service.impl;

import com.example.hotel.model.ConnectionPoolHolder;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.exception.TemporaryApplicationNotFound;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.example.hotel.model.service.exception.Messages.SERVICE_EXCEPTION;
import static com.example.hotel.model.service.exception.Messages.TEMPORARY_APPLICATION_NOT_FOUND;

public class ApartmentServiceImpl implements ApartmentService {
    public static final Logger log = Logger.getLogger(ApartmentServiceImpl.class);

    private final DaoFactory daoFactory;

    public ApartmentServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public List<Apartment> getPreferredApartments(final String clientLogin,
                                                  final int skip,
                                                  final int count) {
        final var connection = ConnectionPoolHolder.getConnection();
        try (var apartmentDao = daoFactory.createApartmentDao(connection);
             var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao(connection)) {
            connection.setAutoCommit(false);
            final var temporaryApplication = temporaryApplicationDao
                    .findByClientLogin(clientLogin)
                    .orElseThrow(() -> new TemporaryApplicationNotFound(TEMPORARY_APPLICATION_NOT_FOUND));
            final var apartments = apartmentDao
                    .findByTemporaryApplication(temporaryApplication, skip, count);
            connection.commit();
            return apartments;
        } catch (final SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public List<Apartment> getApartmentsSortedByPrice(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByPrice(skip, count);
        } catch (final SQLException e) {
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

    @Override
    public Optional<Apartment> getByNumber(long number) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findByNumber(number);
        } catch (final SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public int preferedApartmentsCount(final String clientLogin) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.getPreferredApartmentsCount(clientLogin);
        } catch (final SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    public int count() {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.getCount();
        } catch (final SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }
}
