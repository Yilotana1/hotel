package com.example.hotel.controller.dto;

import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.entity.enums.ApartmentClass;
import org.apache.log4j.Logger;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class TemporaryApplicationDTO {
    public final static Logger log = Logger.getLogger(TemporaryApplicationDTO.class);

    private static final Integer MAX_STAY_LENGTH = 100;
    private static final Integer MAX_NUMBER_OF_PEOPLE = 4;
    private static final Integer MAX_LOGIN_LENGTH = 16;
    private static final int MIN_STAY_LENGTH = 0;
    private static final int MIN_NUMBER_OF_PEOPLE = 0;

    private Integer stayLength;
    private Integer numberOfPeople;
    private ApartmentClass apartmentClass;
    private String clientLogin;

    private TemporaryApplicationDTO() {
    }

    public static TemporaryApplicationDTOBuilder builder() {
        return new TemporaryApplicationDTOBuilder();
    }

    public static class TemporaryApplicationDTOBuilder {
        private final TemporaryApplicationDTO temporaryApplicationDTO = new TemporaryApplicationDTO();

        public TemporaryApplicationDTOBuilder stayLength(final Integer stayLength) {
            temporaryApplicationDTO.setStayLength(stayLength);
            return this;
        }

        public TemporaryApplicationDTOBuilder numberOfPeople(final Integer numberOfPeople) {
            temporaryApplicationDTO.setNumberOfPeople(numberOfPeople);
            return this;
        }

        public TemporaryApplicationDTOBuilder apartmentClass(final ApartmentClass apartmentClass) {
            temporaryApplicationDTO.setApartmentClass(apartmentClass);
            return this;
        }

        public TemporaryApplicationDTOBuilder clientLogin(final String clientLogin) {
            temporaryApplicationDTO.setClientLogin(clientLogin);
            return this;
        }

        public TemporaryApplicationDTO build() {
            return temporaryApplicationDTO;
        }
    }

    public void throwIfNotValid() throws InvalidDataException{
        if (hasNotAllowedNulls()) {
            log.error("Some fields are null");
            throw new InvalidDataException("Some of TemporaryApplicationDTO fields are null");
        }
        if (clientLogin.isBlank() || clientLogin.length() > MAX_LOGIN_LENGTH) {
            log.error("clientLogin == " + clientLogin);
            throw new InvalidDataException("Login must be of appropriate size", "login");
        }
        if (stayLength <= MIN_STAY_LENGTH || stayLength > MAX_STAY_LENGTH) {
            log.error("stayLength == " + stayLength);
            throw new InvalidDataException(
                    format("stayLength must be between %d and %d", MIN_STAY_LENGTH, MAX_STAY_LENGTH),
                    "stay_length");
        }
        if (numberOfPeople <= MIN_NUMBER_OF_PEOPLE || numberOfPeople > MAX_NUMBER_OF_PEOPLE) {
            log.error("numberOfPeople == " + numberOfPeople);
            throw new InvalidDataException(
                    format("numberOfPeople must be between %d and %d", MIN_NUMBER_OF_PEOPLE, MAX_NUMBER_OF_PEOPLE),
                    "number_of_people");
        }
    }

    private boolean hasNotAllowedNulls() throws InvalidDataException {
        try {
            requireNonNull(clientLogin);
            requireNonNull(stayLength);
            requireNonNull(apartmentClass);
            requireNonNull(numberOfPeople);
        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }

    public Integer getStayLength() {
        return stayLength;
    }

    public void setStayLength(final Integer stayLength) {
        this.stayLength = stayLength;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(final Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(final ApartmentClass apartmentClass) {
        this.apartmentClass = apartmentClass;
    }

    public String getClientLogin() {
        return clientLogin;
    }

    public void setClientLogin(final String clientLogin) {
        this.clientLogin = clientLogin;
    }
}
