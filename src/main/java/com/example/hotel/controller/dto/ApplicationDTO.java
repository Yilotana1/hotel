package com.example.hotel.controller.dto;

import com.example.hotel.controller.exception.InvalidDataException;

import java.time.LocalDate;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class ApplicationDTO {
    private static final int MAX_LOGIN_LENGTH = 16;
    public static final int MAX_APARTMENT_NUMBER = 30;
    public static final int MIN_APARTMENT_NUMBER = 1;
    public static final int MIN_STAY_LENGTH = 1;
    private String clientLogin;
    private Integer apartmentNumber;
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

        public ApplicationDTOBuilder apartmentNumber(final int apartmentNumber) {
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

    public void throwIfNotValid() throws InvalidDataException {
        if (hasNotAllowedNulls()) {
            throw new InvalidDataException("Some of application dto fields are null");
        }
        if (clientLogin.length() > MAX_LOGIN_LENGTH || clientLogin.isEmpty()) {
            throw new InvalidDataException("Login must be of appropriate size", "login");
        }
        if (apartmentNumber > MAX_APARTMENT_NUMBER || apartmentNumber < MIN_APARTMENT_NUMBER) {
            throw new InvalidDataException(
                    format("Apartment number must be between %d-%d", MIN_APARTMENT_NUMBER, MAX_APARTMENT_NUMBER),
                    "apartment_number");
        }
        if (stayLength < MIN_STAY_LENGTH) {
            throw new InvalidDataException(
                    format("stayLength must no less than %d", MIN_STAY_LENGTH),
                    "stay_length");
        }
    }

    private boolean hasNotAllowedNulls() {
        try {
            requireNonNull(clientLogin);
            requireNonNull(apartmentNumber);
            requireNonNull(stayLength);
        } catch (NullPointerException e) {
            return true;
        }
        return false;
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

    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(Integer apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
