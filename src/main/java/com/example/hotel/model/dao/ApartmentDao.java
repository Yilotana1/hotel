package com.example.hotel.model.dao;

import com.example.hotel.model.dao.exception.DaoException;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.enums.ApartmentClass;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ApartmentDao extends GenericDao<Apartment> {
    Map<Apartment, String> findApartmentsWithResidentsLogins(int skip, int count) throws DaoException;

    Optional<Apartment> findApartmentByResidentLogin(String login) throws DaoException;

    boolean existsByClassAndNumberOfPeople(ApartmentClass apartmentClass, int numberOfPeople) throws DaoException;

    int getPreferredApartmentsCount(String clientLogin) throws DaoException;

    Collection<Apartment> findByTemporaryApplication(TemporaryApplication temporaryApplication, int skip, int count) throws DaoException;

    Collection<Apartment> findSortedByClass(int skip, int count) throws DaoException;

    Collection<Apartment> findSortedByStatus(int skip, int count) throws DaoException;

    Collection<Apartment> findSortedByPrice(int skip, int count) throws DaoException;

    Collection<Apartment> findSortedByNumberOfPeople(int skip, int count) throws DaoException;

    Optional<Apartment> findByNumber(long number) throws DaoException;

}
