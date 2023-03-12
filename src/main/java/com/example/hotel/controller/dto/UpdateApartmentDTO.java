package com.example.hotel.controller.dto;

import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.entity.enums.ApartmentStatus;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateApartmentDTO {
  public static final Logger log = Logger.getLogger(UpdateApartmentDTO.class);
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
