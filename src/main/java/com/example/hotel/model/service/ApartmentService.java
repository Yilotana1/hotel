package com.example.hotel.model.service;

import com.example.hotel.model.entity.Apartment;

import java.util.List;
import java.util.Optional;

public interface ApartmentService {
    List<Apartment> getApartmentsSortedByPrice(int skip, int count);
    List<Apartment> getApartmentsSortedByPeople(int skip, int count);
    List<Apartment> getApartmentsSortedByClass(int skip, int count);
    List<Apartment> getApartmentsSortedByStatus(int skip, int count);
    Optional<Apartment> getByNumber(long number);
    int count();
}
