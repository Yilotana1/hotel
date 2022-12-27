package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

import static com.example.hotel.model.entity.enums.ApartmentStatus.BOOKED;
import static com.example.hotel.model.entity.enums.ApartmentStatus.BUSY;
import static com.example.hotel.model.entity.enums.ApartmentStatus.FREE;
import static com.example.hotel.model.entity.enums.ApartmentStatus.UNAVAILABLE;

public class Apartment {
    public final static Logger log = Logger.getLogger(Apartment.class);
    private Integer number;
    private Integer floor;
    private ApartmentClass apartmentClass;
    private ApartmentStatus status;
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
                ", apartmentStatus=" + status +
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


        public ApartmentBuilder numberOfPeople(final Integer numberOfPeople) {
            apartment.setNumberOfPeople(numberOfPeople);
            return this;
        }

        public ApartmentBuilder price(final BigDecimal price) {
            apartment.setPrice(price);
            return this;
        }

        public ApartmentBuilder floor(final Integer floor) {
            apartment.setFloor(floor);
            return this;
        }

        public ApartmentBuilder apartmentClass(final ApartmentClass apartmentClass) {
            apartment.setApartmentClass(apartmentClass);
            return this;
        }

        public ApartmentBuilder apartmentStatus(final ApartmentStatus apartmentStatus) {
            apartment.setStatus(apartmentStatus);
            return this;
        }

        public ApartmentBuilder number(final Integer number) {
            apartment.setNumber(number);
            return this;
        }

        public ApartmentBuilder demand(final Integer demand) {
            apartment.setDemand(demand);
            return this;
        }

        public Apartment build() {
            return apartment;
        }

    }

    public boolean isBooked() {
        return getStatus() == BOOKED;
    }

    public void makeUnavailable() {
        log.trace(this + " made unavailable");
        setStatus(UNAVAILABLE);
    }

    public void makeFree() {
        log.trace("Apartment (number = " + this.getNumber() + ") marked free");
        setStatus(FREE);
    }

    public void increaseDemand() {
        setDemand(getDemand() + 1);
    }

    public void book() {
        setStatus(BOOKED);
    }

    public boolean isNotAvailable() {
        return getStatus() != FREE;
    }

    public boolean isNotAllowedToUpdate() {
        return getStatus() != FREE && getStatus() != BUSY || this.isBooked();
    }

    public Integer getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(final Integer numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
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

    public void setStatus(final ApartmentStatus apartmentStatus) {
        this.status = apartmentStatus;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(final Integer number) {
        this.number = number;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(final Integer floor) {
        this.floor = floor;
    }

    public ApartmentClass getApartmentClass() {
        return apartmentClass;
    }

    public void setApartmentClass(final ApartmentClass apartmentClass) {
        this.apartmentClass = apartmentClass;
    }

    public Integer getDemand() {
        return demand;
    }

    public void setDemand(final Integer demand) {
        this.demand = demand;
    }
}
