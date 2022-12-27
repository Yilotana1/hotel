package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
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
import static com.example.hotel.model.entity.enums.ApartmentStatus.BUSY;

public class ApartmentServiceImpl implements ApartmentService {
    public static final Logger log = Logger.getLogger(ApartmentServiceImpl.class);

    private final DaoFactory daoFactory;

    public ApartmentServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Map<Apartment, String> getApartmentsWithResidentLogins(final int skip, final int count) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findApartmentsWithResidentsLogins(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Searching for apartments and its resident's logins somehow failed", e);
        }
    }

    @Override
    public Optional<Apartment> getApartmentByResidentLogin(final String login) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findApartmentByResidentLogin(login);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Searching for apartment by its resident's login somehow failed", e);
        }
    }

    @Override
    public Collection<Apartment> getPreferredApartments(final String clientLogin,
                                                        final int skip,
                                                        final int count) throws ServiceException {
        try (var connection = getConnection();
             var apartmentDao = daoFactory.createApartmentDao(connection);
             var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao(connection)) {
            connection.setAutoCommit(false);
            final var temporaryApplication = temporaryApplicationDao
                    .findByClientLogin(clientLogin)
                    .orElseThrow(() -> new TemporaryApplicationNotFound(
                            "Temporary application for clientLogin = " + clientLogin + "not found"));
            final var apartments = apartmentDao
                    .findByTemporaryApplication(temporaryApplication, skip, count);
            connection.commit();
            return apartments;
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting preferred apartments by clientLogin somehow failed", e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByPrice(final int skip, final int count) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByPrice(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting sorted by price apartments somehow failed", e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByPeople(final int skip, final int count) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByNumberOfPeople(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting sorted by number of people apartments somehow failed", e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByClass(final int skip, final int count) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByClass(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting sorted by class apartments somehow failed", e);
        }
    }

    @Override
    public Collection<Apartment> getApartmentsSortedByStatus(final int skip, final int count) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findSortedByStatus(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting sorted by status apartments somehow failed", e);
        }
    }

    @Override
    public Optional<Apartment> getByNumber(final long number) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.findByNumber(number);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting apartment by its number somehow failed", e);
        }
    }

    @Override
    public int preferredApartmentsCount(final String clientLogin) throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.getPreferredApartmentsCount(clientLogin);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting preferred apartments count by client login somehow failed.", e);
        }
    }

    public int count() throws ServiceException {
        try (var apartmentDao = daoFactory.createApartmentDao()) {
            return apartmentDao.getCount();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException("Getting apartments count somehow failed", e);
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
            if (apartment.isNotAllowedToUpdate()) {
                throw new ApartmentNotAllowedToUpdateException(
                        "Apartment with status =  " + apartment.getStatus() + " not allowed to get updated");
            }
            setFieldsToUpdate(updateApartmentDTO, apartment);
            apartmentDao.update(apartment);
            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Updating apartment's data somehow failed", e);
        }
    }

    private static void setFieldsToUpdate(final UpdateApartmentDTO updateApartmentDTO,
                                          final Apartment apartment) {
        if (updateApartmentDTO.getStatus() == BUSY) {
            apartment.setStatus(updateApartmentDTO.getStatus());
        } else {
            apartment.makeFree();
        }
        apartment.setPrice(updateApartmentDTO.getPrice());
        apartment.setApartmentClass(updateApartmentDTO.getApartmentClass());
    }
}
