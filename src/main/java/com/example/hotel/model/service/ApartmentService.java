package com.example.hotel.model.service;

import com.example.hotel.model.entity.Apartment;

import java.util.List;

public interface ApartmentService {
    List<Apartment> getApartmentsSortedByPrice(int skip, int count);
    List<Apartment> getApartmentsSortedByPeople(int skip, int count);
    List<Apartment> getApartmentsSortedByClass(int skip, int count);
    List<Apartment> getApartmentsSortedByStatus(int skip, int count);
    int count();
}
