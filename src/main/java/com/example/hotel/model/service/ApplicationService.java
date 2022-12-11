package com.example.hotel.model.service;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.model.entity.Application;

import java.time.LocalDate;
import java.util.Optional;

public interface ApplicationService {

    void apply(ApplicationDTO applicationDTO);

    void confirmPayment(long id, LocalDate startDate, LocalDate endDate);

    Optional<Application> getNotApprovedApplicationByClientId(long id);

    Optional<Application> getApprovedApplicationByLogin(String login);

    Optional<Application> getNotApprovedApplicationByLogin(String login);

    Optional<Application> getApplicationToConfirm(String login);
}
