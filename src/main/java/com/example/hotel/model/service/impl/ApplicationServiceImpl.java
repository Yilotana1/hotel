package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.controller.exception.ApplicationNotFoundException;
import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.commons.Constants;
import com.example.hotel.model.service.exception.ApartmentIsNotAvailableException;
import com.example.hotel.model.service.exception.ApartmentsNotFoundException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.exception.NotEnoughMoneyToConfirmException;
import com.example.hotel.model.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;

public class ApplicationServiceImpl implements ApplicationService {


    private final DaoFactory daoFactory;
    public static final Logger log = Logger.getLogger(ApplicationServiceImpl.class);

    public ApplicationServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void removeOutdatedTemporaryApplications() {
        try (var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao();
             var connection = temporaryApplicationDao.getConnection()
        ) {
            connection.setAutoCommit(Constants.AUTO_COMMIT);

            temporaryApplicationDao.deleteByDaysFromCreationDate(Constants.MAX_DURATION_OF_TEMP_APPLICATION);
            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Removing of outdated temporary applications has failed", e);
        }
    }

    @Override
    public void cancelFinishedApprovedApplications() throws ServiceException {
        try (var applicationDao = daoFactory.createApplicationDao();
             var connection = applicationDao.getConnection();
             var apartmentDao = daoFactory.createApartmentDao(connection)) {
            connection.setAutoCommit(Constants.AUTO_COMMIT);

            final var applications = applicationDao.findFinishedApproved();
            for (final var application : applications) {
                application.cancel();
                applicationDao.update(application);
                apartmentDao.update(application.getApartment());
            }

            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Canceling of finished approved applications has failed", e);
        }
    }

    @Override
    public void apply(final ApplicationDTO applicationDTO) throws ServiceException {
        try (var userDao = daoFactory.createUserDao();
             var connection = userDao.getConnection();
             var apartmentDao = daoFactory.createApartmentDao(connection);
             var applicationDao = daoFactory.createApplicationDao(connection);
             var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao(connection)) {
            connection.setAutoCommit(Constants.AUTO_COMMIT);

            temporaryApplicationDao.delete(applicationDTO.getClientLogin());
            throwIfClientHasApplication(applicationDTO.getClientLogin(), applicationDao, temporaryApplicationDao);
            final var apartment = getApartmentOrThrow(applicationDTO, apartmentDao);
            throwIfNotAvailable(apartment);
            final var application = mapToApplication(applicationDTO, userDao, apartment);
            updateApartmentBeforeApply(apartment);
            pushChangesToDB(apartmentDao, applicationDao, apartment, application);

            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Applying process has failed", e);
        }
    }

    @Override
    public long temporaryApplicationsCount() {
        try (var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao()) {
            return temporaryApplicationDao.getCount();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Couldn't get temporaryApplications number", e);
        }
    }

    @Override
    public Collection<TemporaryApplication> getTemporaryApplications(final int skip, final int count) throws ServiceException {
        try (var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao()) {
            return temporaryApplicationDao.findSortedById(skip, count);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Couldn't get collection of temporary applications", e);
        }
    }

    @Override
    public Optional<TemporaryApplication> getTemporaryApplicationByLogin(final String clientLogin) throws ServiceException {
        try (var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao()) {
            return temporaryApplicationDao.findByClientLogin(clientLogin);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Getting temporary application by login failed", e);
        }
    }

    @Override
    public void makeTemporaryApplication(final TemporaryApplicationDTO temporaryApplicationDTO) {
        try (var temporaryApplicationDao = daoFactory.createTemporaryApplicationDao();
             var connection = temporaryApplicationDao.getConnection();
             var apartmentDao = daoFactory.createApartmentDao(connection);
             var applicationDao = daoFactory.createApplicationDao(connection)) {
            connection.setAutoCommit(Constants.AUTO_COMMIT);

            final var clientLogin = temporaryApplicationDTO.getClientLogin();
            throwIfClientHasApplication(clientLogin, applicationDao, temporaryApplicationDao);
            throwIfApartmentsDontExist(temporaryApplicationDTO, apartmentDao);
            final var temporaryApplication = mapToTemporaryApplication(temporaryApplicationDTO);
            temporaryApplicationDao.create(temporaryApplication);

            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Making temporary application has failed", e);
        }
    }

    private static void throwIfApartmentsDontExist(final TemporaryApplicationDTO temporaryApplicationDTO,
                                                   final ApartmentDao apartmentDao) throws DaoException {
        if (!apartmentDao.existsByClassAndNumberOfPeople(
                temporaryApplicationDTO.getApartmentClass(),
                temporaryApplicationDTO.getNumberOfPeople())) {
            final var message = "Apartments with classId = %d and number of People = %d not found";
            throw new ApartmentsNotFoundException(
                    format(message,
                            temporaryApplicationDTO.getApartmentClass().getId(),
                            temporaryApplicationDTO.getNumberOfPeople()));
        }
    }

    private TemporaryApplication mapToTemporaryApplication(final TemporaryApplicationDTO temporaryApplicationDTO) {
        return TemporaryApplication.builder()
                .numberOfPeople(temporaryApplicationDTO.getNumberOfPeople())
                .stayLength(temporaryApplicationDTO.getStayLength())
                .apartmentClass(temporaryApplicationDTO.getApartmentClass())
                .clientLogin(temporaryApplicationDTO.getClientLogin())
                .creationDate(LocalDate.now())
                .build();
    }

    @Override
    public void confirmPayment(final long applicationId, final LocalDate startDate, final LocalDate endDate)
            throws ServiceException {
        try (var applicationDao = daoFactory.createApplicationDao();
             var connection = applicationDao.getConnection();
             var userDao = daoFactory.createUserDao(connection);
             var apartmentDao = daoFactory.createApartmentDao(connection)) {
            connection.setAutoCommit(Constants.AUTO_COMMIT);

            final var application = getApplicationFromDb(applicationId, applicationDao);
            application.approve(startDate, endDate);
            final var apartment = application.getApartment();
            apartment.book();
            final var client = getClient(userDao, application);
            payForApartment(client, application);
            applicationDao.update(application);
            userDao.update(client);
            apartmentDao.update(apartment);

            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Payment confirmation has failed", e);
        }
    }

    private void payForApartment(final User client, final Application application) throws NotEnoughMoneyToConfirmException {
        if (client.getMoney().compareTo(application.getPrice()) < 0) {
            final var message = "Not enough money to pay for apartment: client money = " + client.getMoney()
                    + ", application price = " + application.getPrice();
            log.error(message);
            throw new NotEnoughMoneyToConfirmException(message);
        }
        client.payForApartment(application);
    }

    private User getClient(final UserDao userDao, final Application application) throws DaoException {
        final var login = application.getClientLogin();
        return userDao
                .findByLogin(login)
                .orElseThrow(() -> new LoginIsNotFoundException("Client with login = " + login + " not found"));
    }

    private Application getApplicationFromDb(final long applicationId,
                                             final ApplicationDao applicationDao) throws DaoException {
        return applicationDao
                .findById(applicationId)
                .orElseThrow(
                        () -> new ServiceException(format("Application with id = %d not found", applicationId)));
    }

    private void pushChangesToDB(final ApartmentDao apartmentDao,
                                 final ApplicationDao applicationDao,
                                 final Apartment apartment,
                                 final Application application) throws DaoException {
        apartmentDao.update(apartment);
        applicationDao.create(application);
    }

    private void throwIfNotAvailable(final Apartment apartment) {
        if (apartment.isNotAvailable()) {
            log.error("Apartment is not available");
            throw new ApartmentIsNotAvailableException();
        }
    }

    private Apartment getApartmentOrThrow(final ApplicationDTO applicationDTO, final ApartmentDao apartmentDao) throws DaoException {
        return apartmentDao
                .findById(applicationDTO.getApartmentNumber())
                .orElseThrow(ServiceException::new);
    }

    private void throwIfClientHasApplication(final String clientLogin,
                                             final ApplicationDao applicationDao,
                                             final TemporaryApplicationDao temporaryApplicationDao) throws DaoException, SQLException {
        final var notCanceledApplication = applicationDao
                .findNotCanceledByLogin(clientLogin);
        final var temporaryApplication = temporaryApplicationDao.findByClientLogin(clientLogin);
        if (notCanceledApplication.isPresent() || temporaryApplication.isPresent()) {
            throw new ClientHasNotCanceledApplicationException();
        }
    }

    private void updateApartmentBeforeApply(final Apartment apartment) {
        apartment.makeUnavailable();
        apartment.increaseDemand();
    }

    public Optional<Application> getNotApprovedApplicationByClientId(final long id) throws ServiceException {
        try (var applicationDao = daoFactory.createApplicationDao()) {
            return applicationDao.findNotApprovedByClientId(id);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Searching not approved application by client id has failed", e);
        }
    }

    @Override
    public Optional<Application> getApprovedApplicationByLogin(final String login) {
        try (var applicationDao = daoFactory.createApplicationDao()) {
            return applicationDao.findApprovedByLogin(login);
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Searching approved application by client login has failed", e);
        }
    }

    @Override
    public Optional<Application> getApplicationToConfirm(final String login) throws ServiceException {
        try (var applicationDao = daoFactory.createApplicationDao()) {
            final var application = applicationDao.findNotApprovedByLogin(login);
            if (application.isEmpty()) {
                return Optional.empty();
            }
            final var startDate = LocalDate.now();
            final var endDate = getEndDate(application.get(), startDate);
            application.get().setStartDate(startDate);
            application.get().setEndDate(endDate);
            return application;
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage(), e);
            final var message = "Searching process for application ready to get" +
                    " confirmed by client's login has failed";
            throw new ServiceException(message, e);
        }
    }

    @Override
    public void cancel(final long applicationId) throws ServiceException {
        try (var applicationDao = daoFactory.createApplicationDao();
             var connection = applicationDao.getConnection();
             var apartmentDao = daoFactory.createApartmentDao(connection)) {
            connection.setAutoCommit(Constants.AUTO_COMMIT);

            final var application = applicationDao
                    .findById(applicationId)
                    .orElseThrow(
                            () -> new ApplicationNotFoundException(
                                    "Application with id = " + applicationId + " not found"));

            application.cancel();
            final var apartment = application.getApartment();
            apartment.makeFree();
            apartmentDao.update(apartment);
            applicationDao.update(application);

            connection.commit();
        } catch (final DaoException | SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException("Canceling application by application id has failed", e);
        }
    }

    private static LocalDate getEndDate(final Application application, final LocalDate startDate) {
        return startDate.plusDays(application.getStayLength());
    }

    private Application mapToApplication(final ApplicationDTO applicationDTO,
                                         final UserDao userDao,
                                         final Apartment apartment) throws SQLException {
        final var client = userDao
                .findByLogin(applicationDTO.getClientLogin())
                .orElseThrow(() -> new LoginIsNotFoundException("User with such login not found"));
        final var totalPrice = apartment
                .getPrice()
                .multiply(BigDecimal.valueOf(applicationDTO.getStayLength()));
        return Application.builder()
                .creationDate(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .client(client)
                .apartment(apartment)
                .price(totalPrice)
                .stayLength(applicationDTO.getStayLength())
                .build();
    }
}
