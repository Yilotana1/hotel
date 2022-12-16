package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentClass;

public class TemporaryApplication {
    private Long id;
    private Integer stayLength;
    private Integer numberOfPeople;
    private ApartmentClass apartmentClass;
    private String clientLogin;

    private TemporaryApplication() {
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

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStayLength() {
        return stayLength;
    }

    public void setStayLength(Integer stayLength) {
        this.stayLength = stayLength;
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(ApartmentClass apartmentClass) {
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

        public TemporaryApplication build() {
            return temporaryApplication;
        }
    }
}
