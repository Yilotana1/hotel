package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.enums.ApartmentStatus;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ApartmentNotAllowedToUpdateException;
import com.example.hotel.model.service.exception.ApartmentNotFoundException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.exception.TemporaryApplicationNotFound;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.example.hotel.model.ConnectionPoolHolder.getConnection;
import static com.example.hotel.model.service.exception.Messages.SERVICE_EXCEPTION;
import static com.example.hotel.model.service.exception.Messages.TEMPORARY_APPLICATION_NOT_FOUND;

public class ApartmentServiceImpl implements ApartmentService {
    public static final Logger log = Logger.getLogger(ApartmentServiceImpl.class);

    private final DaoFactory daoFactory;

    public ApartmentServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Map<Apartment, String> getApartmentsWithResidentLogins(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findApartmentsWithResidentsLogins(skip, count);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public Optional<Apartment> getApartmentByResidentLogin(final String login) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findApartmentByResidentLogin(login);
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public Collection<Apartment> getPreferredApartments(final String clientLogin,
                                                        final int skip,
                                                        final int count) {
        try (var connection = getConnection();
             var apartmentDao = daoFactory.createApartmentDao(connection);
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
    public Collection<Apartment> getApartmentsSortedByPrice(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByPrice(skip, count);
        } catch (final SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByPeople(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByNumberOfPeople(skip, count);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByClass(final int skip, final int count) {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByClass(skip, count);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByStatus(final int skip, final int count) {
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
    public int preferredApartmentsCount(final String clientLogin) {
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

    @Override
    public void update(final UpdateApartmentDTO updateApartmentDTO) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            final var connection = apartmentDao.getConnection();
            connection.setAutoCommit(false);
            final var number = updateApartmentDTO.getNumber();
            final var apartment = apartmentDao
                    .findByNumber(number)
                    .orElseThrow(() -> new ApartmentNotFoundException("Apartment " + number + "not found"));
            if (apartment.isBooked() || apartment.isNotAvailable()) {
                throw new ApartmentNotAllowedToUpdateException("Apartment is " + apartment.getStatus());
            }
            setUpdatedFields(updateApartmentDTO, apartment);
            apartmentDao.update(apartment);
            connection.commit();
        } catch (final SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    private static void setUpdatedFields(final UpdateApartmentDTO updateApartmentDTO, final Apartment apartment) {
        if (updateApartmentDTO.getStatus() == ApartmentStatus.BUSY) {
            apartment.setStatus(updateApartmentDTO.getStatus());
        }
        apartment.setPrice(updateApartmentDTO.getPrice());
        apartment.setApartmentClass(updateApartmentDTO.getApartmentClass());
    }
}
