package com.example.hotel.model.dao;

import com.example.hotel.model.entity.Application;
import com.example.hotel.model.entity.enums.ApplicationStatus;

import java.sql.SQLException;
import java.util.List;

public interface ApplicationDAO extends GenericDao<Application> {
    List<Application> findByClientId(long clientId, int skip, int count) throws SQLException;

    List<Application> findSortedById(int skip, int count) throws SQLException;

    List<Application> findSortedByCreationDate(int skip, int count) throws SQLException;
    List<Application> findSortedByCreationDateDescending(int skip, int count) throws SQLException;

    List<Application> findSortedByLastModification(int skip, int count) throws SQLException;
    List<Application> findSortedByLastModificationDescending(int skip, int count) throws SQLException;

    List<Application> findSortedByStartDate(int skip, int count) throws SQLException;
    List<Application> findSortedByStartDateDescending(int skip, int count) throws SQLException;

    List<Application> findSortedByEndDate(int skip, int count) throws SQLException;
    List<Application> findSortedByEndDateDescending(int skip, int count) throws SQLException;

    List<Application> findByStatus(ApplicationStatus status, int skip, int count) throws SQLException;

    List<Application> findByApartmentId(long apartmentId, int skip, int count) throws SQLException;
}
