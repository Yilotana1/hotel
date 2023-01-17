package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.ApplicationDao;
import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.UserDao;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.commons.Constants;
import com.example.hotel.model.service.exception.ApartmentIsNotAvailableException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.hotel.model.entity.enums.ApartmentClass.SUITE;
import static com.example.hotel.model.entity.enums.ApartmentStatus.BOOKED;
import static com.example.hotel.model.entity.enums.ApartmentStatus.FREE;
import static com.example.hotel.model.entity.enums.ApplicationStatus.APPROVED;
import static com.example.hotel.model.entity.enums.ApplicationStatus.NOT_APPROVED;
import static com.example.hotel.model.entity.enums.Role.CLIENT;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ApplicationServiceImplTest {


    private UserDao userDao;
    private ApartmentDao apartmentDao;
    private ApplicationDao applicationDao;
    private TemporaryApplicationDao temporaryApplicationDao;

    private ApplicationService applicationService;

    private Application testApplication;
    private ApplicationDTO applicationDTO;
    private TemporaryApplicationDTO temporaryApplicationDTO;
    private Connection connection;
    private Application approvedApplication;

    private void mockDaoFactory(final DaoFactory daoFactory) {
        when(daoFactory.createUserDao()).thenReturn(userDao);
        when(daoFactory.createApartmentDao()).thenReturn(apartmentDao);
        when(daoFactory.createApplicationDao()).thenReturn(applicationDao);
        when(daoFactory.createTemporaryApplicationDao()).thenReturn(temporaryApplicationDao);
        when(daoFactory.createApartmentDao(any(Connection.class))).thenReturn(apartmentDao);
        when(daoFactory.createApplicationDao(any(Connection.class))).thenReturn(applicationDao);
        when(daoFactory.createTemporaryApplicationDao(any(Connection.class))).thenReturn(temporaryApplicationDao);
        when(daoFactory.createUserDao(any(Connection.class))).thenReturn(userDao);
    }

    private void mockDaoConnection() {
        when(userDao.getConnection()).thenReturn(connection);
        when(apartmentDao.getConnection()).thenReturn(connection);
        when(applicationDao.getConnection()).thenReturn(connection);
        when(temporaryApplicationDao.getConnection()).thenReturn(connection);
    }

    @BeforeEach
    public void init() {
        final var daoFactory = mock(DaoFactory.class);
        connection = mock(Connection.class);
        userDao = mock(UserDao.class);
        apartmentDao = mock(ApartmentDao.class);
        applicationDao = mock(ApplicationDao.class);
        temporaryApplicationDao = mock(TemporaryApplicationDao.class);

        mockDaoFactory(daoFactory);
        mockDaoConnection();
        applicationService = new ApplicationServiceImpl(daoFactory);

        testApplication = Application
                .builder()
                .id(1L)
                .applicationStatus(NOT_APPROVED)
                .price(new BigDecimal(35))
                .startDate(LocalDate.MIN)
                .endDate(LocalDate.MAX)
                .lastModified(LocalDateTime.MAX)
                .creationDate(LocalDateTime.MIN)
                .stayLength(3)
                .apartment(
                        Apartment
                                .builder()
                                .number(1L)
                                .floor(1)
                                .demand(35)
                                .apartmentClass(SUITE)
                                .numberOfPeople(3)
                                .apartmentStatus(FREE)
                                .price(new BigDecimal(35))
                                .build()
                )
                .client(
                        User
                                .builder()
                                .id(1L)
                                .login("john")
                                .status(NON_BLOCKED)
                                .money(new BigDecimal(31))
                                .email("john@gmail.com")
                                .firstname("john")
                                .lastname("smith")
                                .roles(Set.of(CLIENT))
                                .phone("12312312312")
                                .password("fdknlksnflkd")
                                .build()
                )
                .build();
        applicationDTO = ApplicationDTO
                .builder()
                .stayLength(testApplication.getStayLength())
                .apartmentNumber(testApplication.getApartment().getNumber())
                .clientLogin(testApplication.getClientLogin())
                .build();

        temporaryApplicationDTO = TemporaryApplicationDTO
                .builder()
                .clientLogin(testApplication.getClientLogin())
                .apartmentClass(testApplication.getApartment().getApartmentClass())
                .numberOfPeople(testApplication.getApartment().getNumberOfPeople())
                .stayLength(testApplication.getStayLength())
                .build();

        approvedApplication = Application
                .builder()
                .apartment(Apartment
                        .builder()
                        .apartmentStatus(BOOKED)
                        .build())
                .applicationStatus(APPROVED)
                .build();
    }

    @Test
    void removeOutdatedTemporaryApplications_shouldRemoveOutdatedTemporaryApplications() throws SQLException {
        //when
        applicationService.removeOutdatedTemporaryApplications();
        //then
        verify(temporaryApplicationDao, times(1))
                .deleteByDaysFromCreationDate(Constants.MAX_DURATION_OF_TEMP_APPLICATION);
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void cancelFinishedApprovedApplications() throws SQLException {
        //given
        final var applications = List.of(
                approvedApplication,
                approvedApplication,
                approvedApplication);
//when
        when(applicationDao.findFinishedApproved()).thenReturn(applications);

        applicationService.cancelFinishedApprovedApplications();
//then
        assertThat(approvedApplication.isCanceled()).isTrue();
        assertThat(approvedApplication.getApartment().isFree()).isTrue();
        verify(applicationDao, times(applications.size())).update(approvedApplication);
        verify(apartmentDao, times(applications.size())).update(approvedApplication.getApartment());
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void given_ApplicationDTO_apply_shouldCreateNewApplication() throws SQLException {
        final var appliedApartment = Apartment
                .builder()
                .number(testApplication.getApartment().getNumber())
                .floor(testApplication.getApartment().getFloor())
                .demand(testApplication.getApartment().getDemand())
                .apartmentClass(testApplication.getApartment().getApartmentClass())
                .numberOfPeople(testApplication.getApartment().getNumberOfPeople())
                .apartmentStatus(FREE)
                .price(testApplication.getApartment().getPrice())
                .build();
        appliedApartment.makeUnavailable();
        appliedApartment.increaseDemand();

        when(applicationDao.findNotCanceledByLogin(applicationDTO.getClientLogin()))
                .thenReturn(Optional.empty());
        when(apartmentDao.findById(applicationDTO.getApartmentNumber()))
                .thenReturn(Optional.of(testApplication.getApartment()));
        when(userDao.findByLogin(applicationDTO.getClientLogin())).thenReturn(Optional.of(testApplication.getClient()));

        applicationService.apply(applicationDTO);

        verify(temporaryApplicationDao, times(1)).delete(applicationDTO.getClientLogin());
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
        verify(apartmentDao, times(1)).update(appliedApartment);
        verify(applicationDao, times(1)).create(any(Application.class));
    }

    @Test
    void given_ApplicationDTO_apply_shouldThrowExceptionBecauseApplicationForUserWithSpecifiedLoginExists() {
        when(applicationDao.findNotCanceledByLogin(testApplication.getClientLogin()))
                .thenReturn(Optional.of(testApplication));

        assertThrows(
                ClientHasNotCanceledApplicationException.class,
                () -> applicationService.apply(applicationDTO));
    }

    @Test
    void given_ApplicationDTO_apply_shouldThrowExceptionBecauseApartmentIsNotAvailableForApplying() {
        testApplication.getApartment().makeUnavailable();

        when(applicationDao.findNotCanceledByLogin(testApplication.getClientLogin()))
                .thenReturn(Optional.empty());
        when(apartmentDao.findById(applicationDTO.getApartmentNumber()))
                .thenReturn(Optional.of(testApplication.getApartment()));

        assertThrows(
                ApartmentIsNotAvailableException.class,
                () -> applicationService.apply(applicationDTO));
    }

    @Test
    void givenTemporaryApplicationDTO_makeTemporaryApplication_shouldMakeNewTemporaryApplication() {
        final var temporaryApplication = TemporaryApplication
                .builder()
                .stayLength(temporaryApplicationDTO.getStayLength())
                .clientLogin(temporaryApplicationDTO.getClientLogin())
                .creationDate(LocalDate.now())
                .numberOfPeople(temporaryApplicationDTO.getNumberOfPeople())
                .apartmentClass(temporaryApplicationDTO.getApartmentClass())
                .build();

        when(applicationDao.findNotCanceledByLogin(applicationDTO.getClientLogin()))
                .thenReturn(Optional.empty());
        final var apartmentClass = testApplication.getApartment().getApartmentClass();
        final var numberOfPeople = testApplication.getApartment().getNumberOfPeople();
        when(apartmentDao.existsByClassAndNumberOfPeople(apartmentClass, numberOfPeople))
                .thenReturn(true);

        applicationService.makeTemporaryApplication(temporaryApplicationDTO);
        verify(temporaryApplicationDao, times(1)).create(temporaryApplication);
        verify(temporaryApplicationDao, times(1)).create(any(TemporaryApplication.class));
    }

    @Test
    void givenApplicationIdAndStartDateAndEndDate_confirmPayment_shouldConfirmApplication() throws SQLException {
        final var clientMoney = new BigDecimal(300);
        final var applicationPrice = new BigDecimal(10);
        testApplication.setPrice(applicationPrice);
        testApplication.getClient().setMoney(clientMoney);
        final var startDate = LocalDate.of(2022, 10, 1);
        final var endDate = LocalDate.of(2022, 11, 1);

        when(applicationDao.findById(testApplication.getId())).thenReturn(Optional.of(testApplication));
        when(userDao.findByLogin(testApplication.getClientLogin()))
                .thenReturn(Optional.of(testApplication.getClient()));

        applicationService.confirmPayment(
                testApplication.getId(),
                startDate,
                endDate);

        assertThat(testApplication.getStatus()).isEqualTo(APPROVED);
        assertThat(testApplication.getStartDate().get()).isEqualTo(startDate);
        assertThat(testApplication.getEndDate().get()).isEqualTo(endDate);
        assertThat(testApplication.getApartment().isBooked()).isTrue();

        assertThat(testApplication.getClient().getMoney()).isEqualTo(clientMoney.subtract(applicationPrice));
        verify(applicationDao, times(1)).update(testApplication);
        verify(userDao, times(1)).update(testApplication.getClient());
        verify(apartmentDao, times(1)).update(testApplication.getApartment());
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void cancel() throws SQLException {
        //given
        testApplication.getApartment().makeUnavailable();
        testApplication.setStatus(NOT_APPROVED);
        //when
        when(applicationDao.findById(anyLong())).thenReturn(Optional.of(testApplication));

        applicationService.cancel(testApplication.getId());
        //then
        assertThat(testApplication.isCanceled()).isTrue();
        assertThat(testApplication.getApartment().isFree()).isTrue();
        verify(apartmentDao, times(1)).update(any(Apartment.class));
        verify(applicationDao, times(1)).update(any(Application.class));
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }
}