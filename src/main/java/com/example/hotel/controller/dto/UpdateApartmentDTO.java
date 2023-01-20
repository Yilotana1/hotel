package com.example.hotel.controller.dto;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;

public class UpdateApartmentDTO {
    public static final Logger log = Logger.getLogger(UpdateApartmentDTO.class);
    public static final int MIN_NUMBER = 0;
    public static final String NUMBER_REGEX = "[0-9]+";
    public static final String PRICE_REGEX = "[0-9.]+ (\\$|Грн)";
    public static final String APARTMENT_CLASS_REGEX = "[1-4]";
    public static final String WHITESPACE = " ";
    private Long number;
    private ApartmentClass apartmentClass;
    private BigDecimal price;
    private ApartmentStatus status;

    private UpdateApartmentDTO() {
    }

    public static UpdateApartmentDTOBuilder builder() {
        return new UpdateApartmentDTOBuilder();
    }

    public static class UpdateApartmentDTOBuilder {
        private final UpdateApartmentDTO updateApartmentDTO = new UpdateApartmentDTO();

        public UpdateApartmentDTOBuilder number(final Long number) {
            updateApartmentDTO.setNumber(number);
            return this;
        }

        public UpdateApartmentDTOBuilder apartmentClass(final ApartmentClass apartmentClass) {
            updateApartmentDTO.setApartmentClass(apartmentClass);
            return this;
        }

        public UpdateApartmentDTOBuilder status(final ApartmentStatus status) {
            updateApartmentDTO.setStatus(status);
            return this;
        }

        public UpdateApartmentDTOBuilder price(final String priceStr) {
            final var priceValue = priceStr.substring(0, priceStr.indexOf(WHITESPACE));
            if (priceStr.contains("$")) {
                updateApartmentDTO.setPrice(new BigDecimal(priceValue));
            } else {
                final var koef = new BigDecimal(
                        ResourceBundle
                        .getBundle("message", Locale.forLanguageTag("ua"))
                        .getString("currency_koef"));
                updateApartmentDTO.setPrice(new BigDecimal(priceValue).divide(koef));
            }
            return this;
        }


        public UpdateApartmentDTO build() {
            return updateApartmentDTO;
        }
    }

    public static void throwIfNotValid(final HttpServletRequest request) throws InvalidDataException {
        final var number = request.getParameter(RequestParameters.APARTMENT_NUMBER);
        final var classId = request.getParameter(RequestParameters.APARTMENT_CLASS_ID);
        final var price = request.getParameter(RequestParameters.PRICE);
        throwIfNulls(number, classId, price);
        if (!number.matches(NUMBER_REGEX)) {
            throw new InvalidDataException("Apartment number must not be empty and be a number",
                    RequestParameters.APARTMENT_NUMBER);
        }
        if (!price.matches(PRICE_REGEX)) {
            throw new InvalidDataException("Price must not be empty and be double value",
                    RequestParameters.PRICE);
        }
        if (!classId.matches(APARTMENT_CLASS_REGEX)) {
            throw new InvalidDataException("Apartment class id must be between 1 and 4", "class");
        }
        if (parseLong(number) <= MIN_NUMBER) {
            throw new InvalidDataException("Apartment number must be more than zero",
                    RequestParameters.APARTMENT_NUMBER);
        }
        if (new BigDecimal(price.substring(0, price.indexOf(WHITESPACE))).compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidDataException("Apartment price must be more than zero",
                    RequestParameters.PRICE);
        }
    }

    private static void throwIfNulls(final String... args) {
        try {
            for (final var arg : args) {
                requireNonNull(arg);
            }
        } catch (final NullPointerException e) {
            final var message = "Some of the required parameters for UpdateApartmentDTO are missing.";
            log.error(message, e);
            throw new InvalidDataException(message);
        }

    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(final Long number) {
        this.number = number;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(final ApartmentClass apartmentClass) {
        this.apartmentClass = apartmentClass;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public ApartmentStatus getStatus() {
        return status;
    }

    public void setStatus(final ApartmentStatus status) {
        this.status = status;
    }
}
