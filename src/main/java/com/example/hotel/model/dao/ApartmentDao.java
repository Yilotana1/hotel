package com.example.hotel.model.dao;

import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ApartmentDao extends GenericDao<Apartment> {
    Map<Apartment, String> findApartmentsWithResidentsLogins(int skip, int count) throws SQLException;

    Optional<Apartment> findApartmentByResidentLogin(String login) throws SQLException;

    boolean existsByClassAndNumberOfPeople(ApartmentClass apartmentClass, int numberOfPeople) throws SQLException;

    int getPreferredApartmentsCount(String clientLogin) throws SQLException;

    List<Apartment> findByTemporaryApplication(TemporaryApplication temporaryApplication, int skip, int count) throws SQLException;

    List<Apartment> findByFloor(int floor, int skip, int count) throws SQLException;

    List<Apartment> findByClass(ApartmentClass apartmentClass, int skip, int count) throws SQLException;

    List<Apartment> findByStatus(ApartmentStatus apartmentStatus, int skip, int count) throws SQLException;

    List<Apartment> findByNumberOfPeople(int numberOfPeople, int skip, int count) throws SQLException;

    List<Apartment> findSortedByNumber(int skip, int count) throws SQLException;

    List<Apartment> findSortedByNumberDescending(int skip, int count) throws SQLException;

    List<Apartment> findSortedByClass(int skip, int count) throws SQLException;

    List<Apartment> findSortedByClassDescending(int skip, int count) throws SQLException;

    List<Apartment> findSortedByStatus(int skip, int count) throws SQLException;

    List<Apartment> findSortedByPrice(int skip, int count) throws SQLException;

    List<Apartment> findSortedByPriceDescending(int skip, int count) throws SQLException;

    List<Apartment> findSortedByNumberOfPeople(int skip, int count) throws SQLException;

    List<Apartment> findSortedByNumberOfPeopleDescending(int skip, int count) throws SQLException;

    Optional<Apartment> findByNumber(long number) throws SQLException;

}
