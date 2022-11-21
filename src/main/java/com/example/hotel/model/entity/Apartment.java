package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;

import java.math.BigDecimal;

public class Apartment {
    private Integer number;
    private Integer floor;
    private ApartmentClass apartmentClass;
    private ApartmentStatus apartmentStatus;
    private Integer demand;
    private BigDecimal price;
    private Integer numberOfPeople;

    private Apartment() {
    }


    @Override
    public String toString() {
        return "Apartment{" +
                "number=" + number +
                ", floor=" + floor +
                ", apartmentClass=" + apartmentClass +
                ", apartmentStatus=" + apartmentStatus +
                ", demand=" + demand +
                ", price=" + price +
                ", numberOfPeople=" + numberOfPeople +
                '}';
    }

    public static ApartmentBuilder builder() {
        return new ApartmentBuilder();
    }

    public static class ApartmentBuilder {
        private final Apartment apartment = new Apartment();


        public ApartmentBuilder numberOfPeople(Integer numberOfPeople) {
            apartment.setNumberOfPeople(numberOfPeople);
            return this;
        }
        public ApartmentBuilder price(BigDecimal price) {
            apartment.setPrice(price);
            return this;
        }

        public ApartmentBuilder floor(Integer floor) {
            apartment.setFloor(floor);
            return this;
        }

        public ApartmentBuilder apartmentClass(ApartmentClass apartmentClass) {
            apartment.setApartmentClass(apartmentClass);
            return this;
        }

        public ApartmentBuilder apartmentStatus(ApartmentStatus apartmentStatus) {
            apartment.setApartmentStatus(apartmentStatus);
            return this;
        }

        public ApartmentBuilder number(Integer number) {
            apartment.setNumber(number);
            return this;
        }

        public ApartmentBuilder demand(Integer demand) {
            apartment.setDemand(demand);
            return this;
        }

        public Apartment build() {
            return apartment;
        }

    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ApartmentStatus getApartmentStatus() {
        return apartmentStatus;
    }

    public void setApartmentStatus(ApartmentStatus apartmentStatus) {
        this.apartmentStatus = apartmentStatus;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(ApartmentClass apartmentClass) {
        this.apartmentClass = apartmentClass;
    }

    public Integer getDemand() {
        return demand;
    }

    public void setDemand(Integer demand) {
        this.demand = demand;
    }
}
