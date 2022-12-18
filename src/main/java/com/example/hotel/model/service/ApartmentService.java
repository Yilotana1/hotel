package com.example.hotel.model.service;

import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.model.entity.Apartment;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface ApartmentService {

    Map<Apartment, String> getApartmentsWithResidentLogins(int skip, int count);
    Optional<Apartment> getApartmentByResidentLogin(String login);
    Collection<Apartment> getPreferredApartments(String clientLogin, int skip, int count);

    Collection<Apartment> getApartmentsSortedByPrice(int skip, int count);

    Collection<Apartment> getApartmentsSortedByPeople(int skip, int count);

    Collection<Apartment> getApartmentsSortedByClass(int skip, int count);

    Collection<Apartment> getApartmentsSortedByStatus(int skip, int count);

    Optional<Apartment> getByNumber(long number);

    int preferredApartmentsCount(String clientLogin);

    int count();

    void update(UpdateApartmentDTO updateApartmentDTO);
}
