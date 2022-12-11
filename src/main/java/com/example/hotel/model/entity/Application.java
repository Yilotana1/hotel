package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.ApplicationStatus;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.hotel.model.entity.enums.ApplicationStatus.APPROVED;
import static com.example.hotel.model.entity.enums.ApplicationStatus.CANCELED;
import static com.example.hotel.model.entity.enums.ApplicationStatus.NOT_APPROVED;

public class Application {

    public final static Logger log = Logger.getLogger(Application.class);
    private Long id;
    private User client;
    private Apartment apartment;
    private ApplicationStatus status = NOT_APPROVED;
    private BigDecimal price;
    private LocalDateTime creationDate;
    private LocalDateTime lastModified;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer stayLength;

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

        public ApplicationBuilder startDate(LocalDate startDate) {
            application.setStartDate(startDate);
            return this;
        }

        public ApplicationBuilder endDate(LocalDate endDate) {
            application.setEndDate(endDate);
            return this;
        }

        public ApplicationBuilder stayLength(Integer stayLength) {
            application.setStayLength(stayLength);
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
                ", stayLength=" + stayLength +
                '}';
    }

    public void approve(final LocalDate startDate, final LocalDate endDate) {
        setStatus(APPROVED);
        setStartDate(startDate);
        setEndDate(endDate);
        log.trace("application got approved");
    }

    public void cancel() {
        setStatus(CANCELED);
        log.trace("application got canceled");
    }

    public String getClientLogin() {
        return client.getLogin();
    }

    public Optional<LocalDate> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Integer getStayLength() {
        return stayLength;
    }

    private void setStayLength(Integer stayLength) {
        this.stayLength = stayLength;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public void setEndDate(LocalDate endDate) {
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
