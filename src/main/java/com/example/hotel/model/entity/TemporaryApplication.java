package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentClass;

import java.time.LocalDate;

public class TemporaryApplication {
    private Long id;
    private Integer stayLength;
    private Integer numberOfPeople;
    private ApartmentClass apartmentClass;
    private String clientLogin;
    private LocalDate creationDate;

    private TemporaryApplication() {
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getClientLogin() {
        return clientLogin;
    }

    public void setClientLogin(String clientLogin) {
        this.clientLogin = clientLogin;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public static ApartmentRequestBuilder builder() {
        return new ApartmentRequestBuilder();
    }

    public static class ApartmentRequestBuilder {
        private final TemporaryApplication temporaryApplication = new TemporaryApplication();

        public ApartmentRequestBuilder id(final Long id) {
            temporaryApplication.setId(id);
            return this;
        }

        public ApartmentRequestBuilder stayLength(final Integer stayLength) {
            temporaryApplication.setStayLength(stayLength);
            return this;
        }

        public ApartmentRequestBuilder numberOfPeople(final Integer numberOfPeople) {
            temporaryApplication.setNumberOfPeople(numberOfPeople);
            return this;
        }

        public ApartmentRequestBuilder apartmentClass(final ApartmentClass apartmentClass) {
            temporaryApplication.setApartmentClass(apartmentClass);
            return this;
        }

        public ApartmentRequestBuilder clientLogin(final String login) {
            temporaryApplication.setClientLogin(login);
            return this;
        }
        public ApartmentRequestBuilder creationDate(final LocalDate creationDate) {
            temporaryApplication.setCreationDate(creationDate);
            return this;
        }

        public TemporaryApplication build() {
            return temporaryApplication;
        }
    }
}
