package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.model.ConnectionPoolHolder;
import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ApartmentIsNotAvailableException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.exception.NotEnoughMoneyToConfirmException;
import com.example.hotel.model.service.exception.ServiceException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.hotel.model.service.exception.Messages.NO_APPLICATION_FOUND;
import static com.example.hotel.model.service.exception.Messages.SERVICE_EXCEPTION;
import static java.lang.String.format;

public class ApplicationServiceImpl implements ApplicationService {

    private final DaoFactory daoFactory;
    public final static Logger log = Logger.getLogger(ApplicationServiceImpl.class);

    public ApplicationServiceImpl(final DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public void apply(final ApplicationDTO applicationDTO) throws ServiceException {
        final var connection = ConnectionPoolHolder.getConnection();
        try (final var userDao = daoFactory.createUserDao(connection);
             final var apartmentDao = daoFactory.createApartmentDao(connection);
             final var applicationDao = daoFactory.createApplicationDao(connection)) {
            connection.setAutoCommit(false);
            throwIfClientAlreadyHasApplication(applicationDTO, applicationDao);
            final var apartment = getApartmentOrThrow(applicationDTO, apartmentDao);
            throwIfNotAvailable(apartment);
            final var application = mapToApplication(applicationDTO, userDao, apartment);
            updateApartmentBeforeApply(apartment);
            pushChangesToDB(apartmentDao, applicationDao, apartment, application);
            connection.commit();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    @Override
    public void confirmPayment(final long applicationId, final LocalDate startDate, final LocalDate endDate) throws ServiceException {
        final var connection = ConnectionPoolHolder.getConnection();
        try (final var applicationDao = daoFactory.createApplicationDao(connection);
             final var userDao = daoFactory.createUserDao(connection);
             final var apartmentDao = daoFactory.createApartmentDao(connection)) {
            connection.setAutoCommit(false);
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
        } catch (final SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION, e);
        }
    }

    private void payForApartment(final User client, final Application application) throws NotEnoughMoneyToConfirmException {
        if (client.getMoney().compareTo(application.getPrice()) < 0) {
            throw new NotEnoughMoneyToConfirmException(client, application);
        }
        client.payForApartment(application);
    }

    private User getClient(UserDao userDao, Application application) throws SQLException {
        return userDao
                .findByLogin(application.getClientLogin())
                .orElseThrow(LoginIsNotFoundException::new);
    }

    private Application getApplicationFromDb(long applicationId, ApplicationDao applicationDao) throws SQLException {
        return applicationDao
                .findById(applicationId)
                .orElseThrow(() -> new ServiceException(format(NO_APPLICATION_FOUND, applicationId)));
    }

    private void pushChangesToDB(final ApartmentDao apartmentDao,
                                 final ApplicationDao applicationDao,
                                 final Apartment apartment,
                                 final Application application) throws SQLException {
        apartmentDao.update(apartment);
        applicationDao.create(application);
    }

    private void throwIfNotAvailable(Apartment apartment) {
        if (apartment.isNotAvailable()) {
            log.error("Apartment is not available");
            throw new ApartmentIsNotAvailableException();
        }
    }

    private Apartment getApartmentOrThrow(ApplicationDTO applicationDTO, ApartmentDao apartmentDao) throws SQLException {
        return apartmentDao
                .findById(applicationDTO.getApartmentNumber())
                .orElseThrow(ServiceException::new);
    }

    private void throwIfClientAlreadyHasApplication(ApplicationDTO applicationDTO, ApplicationDao applicationDao) throws SQLException {
        final var clientLogin = applicationDTO.getClientLogin();
        final var notCanceledApplication = applicationDao
                .findNotCanceledByLogin(clientLogin);
        if (notCanceledApplication.isPresent()) {
            throw new ClientHasNotCanceledApplicationException();
        }
    }

    private void updateApartmentBeforeApply(Apartment apartment) {
        apartment.markAsUnavailable();
        apartment.increaseDemand();
    }

    //TODO Make a notification in profile that client has paid application and startDATE,endDate
    //TODO Add "cancel" button in application-invoice and cancel functionality. Application must change status to cancel. Apartment must become available.
    //TODO Read about ServletContextListener. Try to implement checking if confirmation time is expired and then cancel application.
    //TODO Also you need to check if endDate is expired and then cancel application if needed.
    public Optional<Application> getNotApprovedApplicationByClientId(long id) throws ServiceException {
        try (final var applicationDao = daoFactory.createApplicationDao()) {
            return applicationDao.findNotApprovedByClientId(id);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION);
        }
    }

    @Override
    public Optional<Application> getApprovedApplicationByLogin(String login) {
        try (final var applicationDao = daoFactory.createApplicationDao()) {
            return applicationDao.findApprovedByLogin(login);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION);
        }
    }

    @Override
    public Optional<Application> getApplicationToConfirm(final String login) throws ServiceException {
        try (final var applicationDao = daoFactory.createApplicationDao()) {
            final var application = applicationDao.findNotApprovedByLogin(login);
            if (application.isEmpty()) {
                return Optional.empty();
            }
            final var startDate = LocalDate.now();
            final var endDate = getEndDate(application.get(), startDate);
            application.get().setStartDate(startDate);
            application.get().setEndDate(endDate);
            return application;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION);
        }
    }

    private static LocalDate getEndDate(Application application, LocalDate startDate) {
        return LocalDate
                .ofYearDay(startDate.getYear(), startDate.getDayOfYear() + application.getStayLength());
    }

    @Override
    public Optional<Application> getNotApprovedApplicationByLogin(String login) {
        try (final var applicationDao = daoFactory.createApplicationDao()) {
            return applicationDao.findNotApprovedByLogin(login);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new ServiceException(SERVICE_EXCEPTION);
        }
    }


    private Application mapToApplication(final ApplicationDTO applicationDTO,
                                         final UserDao userDao,
                                         final Apartment apartment) throws SQLException {
        final var client = userDao
                .findByLogin(applicationDTO.getClientLogin())
                .orElseThrow(SQLException::new);
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
