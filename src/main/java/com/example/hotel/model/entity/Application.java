package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApplicationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Period;

public class Application {

    private Long id;
    private User client;
    private Apartment apartment;
    private ApplicationStatus status;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime lastModified;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static ApplicationBuilder builder() {
        return new ApplicationBuilder();
    }

    private Application() {
    }

    public static class ApplicationBuilder {
        private final Application application = new Application();

        public ApplicationBuilder id(long id) {
            application.setId(id);
            return this;
        }

        public ApplicationBuilder client(User client) {
            application.setClient(client);
            return this;
        }

        public ApplicationBuilder apartment(Apartment apartment) {
            application.setApartment(apartment);
            return this;
        }

        public ApplicationBuilder applicationStatus(ApplicationStatus applicationStatus) {
            application.setStatus(applicationStatus);
            return this;
        }

        public ApplicationBuilder price(BigDecimal price) {
            application.setPrice(price);
            return this;
        }

        public ApplicationBuilder creationDate(LocalDateTime creationDate) {
            application.setCreationDate(creationDate);
            return this;
        }

        public ApplicationBuilder lastModified(LocalDateTime lastModified) {
            application.setLastModified(lastModified);
            return this;
        }

        public ApplicationBuilder startDate(LocalDateTime startDate) {
            application.setStartDate(startDate);
            return this;
        }

        public ApplicationBuilder endDate(LocalDateTime endDate) {
            application.setEndDate(endDate);
            return this;
        }


        public Application build() {
            return application;
        }
    }


    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", client=" + client +
                ", apartment=" + apartment +
                ", status=" + status +
                ", price=" + price +
                ", creationDate=" + creationDate +
                ", lastModified=" + lastModified +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", duration=" + getDuration() +
                '}';
    }

    public Period getDuration() {
        return Period.between(getStartDate().toLocalDate(), getEndDate().toLocalDate());
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }


}
