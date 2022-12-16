package com.example.hotel.controller.dto;

import com.example.hotel.model.entity.enums.ApartmentClass;

public class ApartmentDTO {
    private Integer numberOfPeople;
    private ApartmentClass apartmentClass;
    private Integer stayLength;

    private ApartmentDTO() {
    }

    public static ApartmentDTOBuilder builder() {
        return new ApartmentDTOBuilder();
    }
    public static class ApartmentDTOBuilder {
        private final ApartmentDTO apartmentDTO = new ApartmentDTO();

        public ApartmentDTOBuilder numberOfPeople(final Integer numberOfPeople) {
            apartmentDTO.setNumberOfPeople(numberOfPeople);
            return this;
        }

        public ApartmentDTOBuilder apartmentClass(final ApartmentClass apartmentClass) {
            apartmentDTO.setApartmentClass(apartmentClass);
            return this;
        }

        public ApartmentDTOBuilder stayLength(final Integer stayLength) {
            apartmentDTO.setStayLength(stayLength);
            return this;
        }

        public ApartmentDTO build() {
            return apartmentDTO;
        }
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

    public Integer getStayLength() {
        return stayLength;
    }

    public void setStayLength(Integer stayLength) {
        this.stayLength = stayLength;
    }
}
