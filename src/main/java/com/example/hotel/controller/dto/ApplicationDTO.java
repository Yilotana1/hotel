package com.example.hotel.controller.dto;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.exception.InvalidDataException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.time.LocalDate;

import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;

public class ApplicationDTO {
    public final static Logger log = Logger.getLogger(ApplicationDTO.class);
    public static final String NUMBER_REGEX = "[0-9]+";
    private String clientLogin;
    private long apartmentNumber;
    private final LocalDate creationDate = LocalDate.now();
    private Integer stayLength;

    private ApplicationDTO() {
    }

    public static ApplicationDTOBuilder builder() {
        return new ApplicationDTOBuilder();
    }

    public static class ApplicationDTOBuilder {
        private final ApplicationDTO applicationDTO = new ApplicationDTO();

        public ApplicationDTOBuilder clientLogin(final String clientLogin) {
            applicationDTO.setClientLogin(clientLogin);
            return this;
        }

        public ApplicationDTOBuilder apartmentNumber(final long apartmentNumber) {
            applicationDTO.setApartmentNumber(apartmentNumber);
            return this;
        }

        public ApplicationDTOBuilder stayLength(final Integer stayLength) {
            applicationDTO.setStayLength(stayLength);
            return this;
        }

        public ApplicationDTO build() {
            return applicationDTO;
        }

    }

    public static void throwIfNotValid(final HttpServletRequest request) throws InvalidDataException {
        final var apartmentNumber = request.getParameter(RequestParameters.APARTMENT_NUMBER);
        final var stayLength = request.getParameter(RequestParameters.STAY_LENGTH);
        throwIfNulls(apartmentNumber, stayLength);
        if (!apartmentNumber.matches(NUMBER_REGEX)) {
            throw new InvalidDataException("apartmentNumber must be a number", RequestParameters.APARTMENT_NUMBER);
        }
        if (!stayLength.matches(NUMBER_REGEX)) {
            throw new InvalidDataException("stayLength must be a number", RequestParameters.STAY_LENGTH);
        }
        if (parseLong(apartmentNumber) <= 0) {
            throw new InvalidDataException("apartmentNumber must be more that 0", RequestParameters.APARTMENT_NUMBER);
        }
        if (parseLong(stayLength) <= 0) {
            throw new InvalidDataException("stayLength must be more that 0", RequestParameters.STAY_LENGTH);
        }
    }

    private static void throwIfNulls(final String... args) throws InvalidDataException {
        try {
            for (final var arg : args) {
                requireNonNull(arg);
            }
        } catch (final NullPointerException e) {
            final var message = "Some of the required parameters for ApplicationDTO are missing.";
            log.error(message, e);
            throw new InvalidDataException(message);
        }
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Integer getStayLength() {
        return stayLength;
    }

    public void setStayLength(Integer stayLength) {
        this.stayLength = stayLength;
    }

    public String getClientLogin() {
        return clientLogin;
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }

    public long getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(long apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
