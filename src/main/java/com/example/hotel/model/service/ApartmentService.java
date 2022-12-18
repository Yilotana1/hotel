package com.example.hotel.model.service;

import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.exception.ServiceException;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ApartmentService {

    Map<Apartment, String> getApartmentsWithResidentLogins(int skip, int count) throws ServiceException;

    Optional<Apartment> getApartmentByResidentLogin(String login) throws ServiceException;

    Collection<Apartment> getPreferredApartments(String clientLogin, int skip, int count) throws ServiceException;

    Collection<Apartment> getApartmentsSortedByPrice(int skip, int count) throws ServiceException;

    Collection<Apartment> getApartmentsSortedByPeople(int skip, int count) throws ServiceException;

    Collection<Apartment> getApartmentsSortedByClass(int skip, int count) throws ServiceException;

    Collection<Apartment> getApartmentsSortedByStatus(int skip, int count) throws ServiceException;

    Optional<Apartment> getByNumber(long number) throws ServiceException;

    int preferredApartmentsCount(String clientLogin) throws ServiceException;

    int count() throws ServiceException;

    void update(UpdateApartmentDTO updateApartmentDTO) throws ServiceException;
}
