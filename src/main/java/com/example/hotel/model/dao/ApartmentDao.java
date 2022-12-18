package com.example.hotel.model.dao;

import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.enums.ApartmentClass;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ApartmentDao extends GenericDao<Apartment> {
    Map<Apartment, String> findApartmentsWithResidentsLogins(int skip, int count) throws SQLException;

    Optional<Apartment> findApartmentByResidentLogin(String login) throws SQLException;

    boolean existsByClassAndNumberOfPeople(ApartmentClass apartmentClass, int numberOfPeople) throws SQLException;

    int getPreferredApartmentsCount(String clientLogin) throws SQLException;

    Collection<Apartment> findByTemporaryApplication(TemporaryApplication temporaryApplication, int skip, int count) throws SQLException;

    Collection<Apartment> findSortedByClass(int skip, int count) throws SQLException;

    Collection<Apartment> findSortedByStatus(int skip, int count) throws SQLException;

    Collection<Apartment> findSortedByPrice(int skip, int count) throws SQLException;

    Collection<Apartment> findSortedByNumberOfPeople(int skip, int count) throws SQLException;

    Optional<Apartment> findByNumber(long number) throws SQLException;

}
