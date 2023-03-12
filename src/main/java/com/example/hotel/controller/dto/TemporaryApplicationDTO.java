package com.example.hotel.controller.dto;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.entity.enums.ApartmentClass;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class TemporaryApplicationDTO {
    public final static Logger log = Logger.getLogger(TemporaryApplicationDTO.class);

    private static final Integer MAX_STAY_LENGTH = 100;
    private static final Integer MAX_NUMBER_OF_PEOPLE = 4;
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

    public static void throwIfNotValid(final HttpServletRequest request) throws InvalidDataException {
        final var stayLength = request.getParameter(RequestParameters.STAY_LENGTH);
        final var apartmentClass = request.getParameter(RequestParameters.APARTMENT_CLASS_ID);
        final var numberOfPeople = request.getParameter(RequestParameters.NUMBER_OF_PEOPLE);
        throwIfNulls(stayLength, apartmentClass, numberOfPeople);
        if (stayLength.isEmpty()
                || parseInt(stayLength) <= MIN_STAY_LENGTH
                        || parseInt(stayLength) > MAX_STAY_LENGTH) {
            final var message = format(
                    "Stay Length must be between %d and %d",
                    MIN_STAY_LENGTH, MAX_STAY_LENGTH);

            log.error(message);
            throw new InvalidDataException(
                    message,
                    "stay_length");
        }
        if (numberOfPeople.isEmpty() || parseInt(numberOfPeople) <= MIN_NUMBER_OF_PEOPLE
                || parseInt(numberOfPeople) > MAX_NUMBER_OF_PEOPLE) {
            final var message = format("numberOfPeople must be between %d and %d", MIN_NUMBER_OF_PEOPLE, MAX_NUMBER_OF_PEOPLE);
            log.error(message);
            throw new InvalidDataException(
                    message,
                    RequestParameters.NUMBER_OF_PEOPLE);
        }
    }

    private static void throwIfNulls(final String... args) throws InvalidDataException {
        try {
            for (final var arg : args) {
                requireNonNull(arg);
            }
        } catch (final NullPointerException e) {
            final var message = "Some of the required parameters for TemporaryApplicationDTO are missing.";
            log.error(message, e);
            throw new InvalidDataException(message);
        }

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
