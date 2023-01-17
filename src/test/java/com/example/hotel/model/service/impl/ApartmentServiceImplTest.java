package com.example.hotel.model.service.impl;

import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.model.dao.ApartmentDao;
import com.example.hotel.model.dao.TemporaryApplicationDao;
import com.example.hotel.model.dao.factory.DaoFactory;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.commons.Constants;
import com.example.hotel.model.service.exception.ApartmentNotAllowedToUpdateException;
import com.example.hotel.model.service.exception.ApartmentNotFoundException;
import com.example.hotel.model.service.exception.TemporaryApplicationNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static com.example.hotel.model.entity.enums.ApartmentClass.STANDARD;
import static com.example.hotel.model.entity.enums.ApartmentClass.SUITE;
import static com.example.hotel.model.entity.enums.ApartmentStatus.BOOKED;
import static com.example.hotel.model.entity.enums.ApartmentStatus.BUSY;
import static com.example.hotel.model.entity.enums.ApartmentStatus.FREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ApartmentServiceImplTest {

    private ApartmentDao apartmentDao;
    private TemporaryApplicationDao temporaryApplicationDao;
    private Connection connection;

    private ApartmentService apartmentService;
    private Apartment testApartment;
    private TemporaryApplication testTemporaryApplication;
    private UpdateApartmentDTO updateApartmentDTO;
    private static final String LOGIN = "john";

    @BeforeEach
    public void init() {
        apartmentDao = mock(ApartmentDao.class);
        temporaryApplicationDao = mock(TemporaryApplicationDao.class);
        final var daoFactory = mock(DaoFactory.class);
        connection = mock(Connection.class);
        when(daoFactory.createApartmentDao()).thenReturn(apartmentDao);
        when(daoFactory.createTemporaryApplicationDao(connection)).thenReturn(temporaryApplicationDao);
        when(apartmentDao.getConnection()).thenReturn(connection);

        apartmentService = new ApartmentServiceImpl(daoFactory);

        testApartment = Apartment
                .builder()
                .number(1L)
                .floor(1)
                .apartmentStatus(FREE)
                .price(new BigDecimal(321))
                .apartmentClass(STANDARD)
                .numberOfPeople(3)
                .demand(3)
                .build();

        testTemporaryApplication = TemporaryApplication
                .builder()
                .id(1L)
                .apartmentClass(testApartment.getApartmentClass())
                .numberOfPeople(testApartment.getNumberOfPeople())
                .creationDate(LocalDate.MAX)
                .stayLength(3)
                .clientLogin(LOGIN)
                .build();

        updateApartmentDTO = UpdateApartmentDTO
                .builder()
                .number(1L)
                .status(BUSY)
                .price("3123 $")
                .apartmentClass(SUITE)
                .build();
    }


    @Test
    void givenSkipAndCount_getApartmentsWithResidentLogins_shouldReturnMapOfApartmentsAndItsLogins() {
        //given
        final var skip = 1;
        final var count = 1;
        final var map = new HashMap<Apartment, String>();
        //when
        when(apartmentDao.findApartmentsWithResidentsLogins(skip, count)).thenReturn(map);
        //then
        assertThat(apartmentService.getApartmentsWithResidentLogins(skip, count)).isEqualTo(map);
        verify(apartmentDao, times(1)).findApartmentsWithResidentsLogins(skip, count);
    }

    @Test
    void givenResidentLogin_getApartmentByResidentLogin_shouldReturnApartmentByItsResidentLogin() {
        //when
        when(apartmentDao.findApartmentByResidentLogin(LOGIN)).thenReturn(Optional.of(testApartment));
        //then
        assertThat(apartmentService.getApartmentByResidentLogin(LOGIN)).isEqualTo(Optional.of(testApartment));
        verify(apartmentDao, times(1)).findApartmentByResidentLogin(LOGIN);
    }

    @Test
    void givenClientLoginAndSkipAndCount_getPreferredApartments_shouldReturnCollectionOfPreferredApartments()
            throws SQLException {
        //given
        final var preferredApartments = new ArrayList<Apartment>();
        final var skip = 1;
        final var count = 1;
        //when
        when(temporaryApplicationDao.findByClientLogin(LOGIN)).thenReturn(Optional.of(testTemporaryApplication));
        when(apartmentDao.findByTemporaryApplication(testTemporaryApplication, skip, count))
                .thenReturn(preferredApartments);

        //then
        assertThat(apartmentService.getPreferredApartments(LOGIN, skip, count)).isEqualTo(preferredApartments);
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void givenWrongClientLogin_getPreferredApartments_shouldThrowTemporaryApplicationNotFoundException() {
        //given
        final var skip = 1;
        final var count = 1;
        //when
        when(temporaryApplicationDao.findByClientLogin(LOGIN)).thenReturn(Optional.empty());
        //then
        assertThrows(
                TemporaryApplicationNotFound.class,
                () -> apartmentService.getPreferredApartments(LOGIN, skip, count));
    }


    @Test
    void givenSkipAndCount_getApartmentsSortedByPrice_shouldReturnCollectionOfApartmentsSortedByPriceValue() {
        //given
        final var skip = 1;
        final var count = 1;
        final var apartments = new ArrayList<Apartment>();
        //when
        when(apartmentDao.findSortedByPrice(skip, count)).thenReturn(apartments);
        //then
        assertThat(apartmentService.getApartmentsSortedByClass(skip, count)).isEqualTo(apartments);
    }

    @Test
    void givenSkipAndCount_getApartmentsSortedByPeople_shouldReturnCollectionOfApartmentsSortedByNumberOfPeople() {
        //given
        final var skip = 1;
        final var count = 1;
        final var apartments = new ArrayList<Apartment>();
        //when
        when(apartmentDao.findSortedByNumberOfPeople(skip, count)).thenReturn(apartments);
        //then
        assertThat(apartmentService.getApartmentsSortedByPeople(skip, count)).isEqualTo(apartments);
    }

    @Test
    void givenSkipAndCount_getApartmentsSortedByClass_shouldReturnCollectionOfApartmentsSortedByItsClass() {
        //given
        final var skip = 1;
        final var count = 1;
        final var apartments = new ArrayList<Apartment>();
        //when
        when(apartmentDao.findSortedByClass(skip, count)).thenReturn(apartments);
        //then
        assertThat(apartmentService.getApartmentsSortedByClass(skip, count)).isEqualTo(apartments);
    }

    @Test
    void givenSkipAndCount_getApartmentsSortedByStatus_shouldReturnCollectionOfApartmentsSortedByItsStatus() {
        //given
        final var skip = 1;
        final var count = 1;
        final var apartments = new ArrayList<Apartment>();
        //when
        when(apartmentDao.findSortedByStatus(skip, count)).thenReturn(apartments);
        //then
        assertThat(apartmentService.getApartmentsSortedByStatus(skip, count)).isEqualTo(apartments);
    }

    @Test
    void preferredApartmentsCount() {
        //given
        final var countValue = 5;
        //when
        when(apartmentDao.getPreferredApartmentsCount(LOGIN)).thenReturn(countValue);
        //then
        assertThat(apartmentService.preferredApartmentsCount(LOGIN)).isEqualTo(countValue);
    }


    @Test
    void givenUpdateApartmentDTO_update_shouldUpdateApartment() throws SQLException {
        //when
        when(apartmentDao.findByNumber(updateApartmentDTO.getNumber())).thenReturn(Optional.of(testApartment));

        //then
        apartmentService.update(updateApartmentDTO);

        assertThat(testApartment.getPrice()).isEqualTo(updateApartmentDTO.getPrice());
        assertThat(testApartment.getApartmentClass()).isEqualTo(updateApartmentDTO.getApartmentClass());
        assertThat(testApartment.getStatus()).isEqualTo(updateApartmentDTO.getStatus());
        verify(apartmentDao, times(1)).update(any(Apartment.class));
        verify(connection, times(1)).setAutoCommit(Constants.AUTO_COMMIT);
        verify(connection, times(1)).commit();
    }

    @Test
    void givenUpdateApartmentDTOWithWrongNumber_update_shouldThrowApartmentNotFoundException() {
        //when
        when(apartmentDao.findByNumber(updateApartmentDTO.getNumber())).thenReturn(Optional.empty());

        //then
        assertThrows(ApartmentNotFoundException.class, () -> apartmentService.update(updateApartmentDTO));
    }

    @Test
    void givenNotAllowedToUpdateApartment_update_shouldThrowApartmentNotAllowedToUpdateException() {
        testApartment.setStatus(BOOKED);
        //when
        when(apartmentDao.findByNumber(updateApartmentDTO.getNumber())).thenReturn(Optional.of(testApartment));

        //then
        assertThrows(ApartmentNotAllowedToUpdateException.class, () -> apartmentService.update(updateApartmentDTO));
    }

}