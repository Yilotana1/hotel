package com.example.hotel.model.service;

import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.entity.Application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ApplicationService {

    void removeOutdatedTemporaryApplications();
    void cancelOutdatedApprovedApplications();
    void apply(ApplicationDTO applicationDTO);
    long temporaryApplicationsCount();
    List<TemporaryApplication> getTemporaryApplications(int skip, int count);
    Optional<TemporaryApplication> getTemporaryApplicationByLogin(String clientLogin);

    void makeTemporaryApplication(TemporaryApplicationDTO temporaryApplicationDTO);

    void confirmPayment(long id, LocalDate startDate, LocalDate endDate);

    Optional<Application> getNotApprovedApplicationByClientId(long id);

    Optional<Application> getApprovedApplicationByLogin(String login);

    Optional<Application> getNotApprovedApplicationByLogin(String login);

    Optional<Application> getApplicationToConfirm(String login);

    void cancel(long applicationId);
}
