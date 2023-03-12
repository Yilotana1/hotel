package com.example.hotel.controller.dto;

import org.apache.log4j.Logger;

public class ApplicationDTO {
  public final static Logger log = Logger.getLogger(ApplicationDTO.class);
  private String clientLogin;
  private long apartmentNumber;
  private Integer stayLength;

  private ApplicationDTO() {
  }

  public static ApplicationDTOBuilder builder() {
    return new ApplicationDTOBuilder();
  }

  public static class ApplicationDTOBuilder {
    private final ApplicationDTO applicationDTO = new ApplicationDTO();

    public ApplicationDTOBuilder clientLogin(final String clientLogin) {
      applicationDTO.setClientLogin(clientLogin);
      return this;
    }

    public ApplicationDTOBuilder apartmentNumber(final long apartmentNumber) {
      applicationDTO.setApartmentNumber(apartmentNumber);
      return this;
    }

    public ApplicationDTOBuilder stayLength(final Integer stayLength) {
      applicationDTO.setStayLength(stayLength);
      return this;
    }

    public ApplicationDTO build() {
      return applicationDTO;
    }

  }

  public Integer getStayLength() {
    return stayLength;
  }

  public void setStayLength(final Integer stayLength) {
    this.stayLength = stayLength;
  }

  public String getClientLogin() {
    return clientLogin;
  }

  public void setClientLogin(final String clientLogin) {
    this.clientLogin = clientLogin;
  }

  public long getApartmentNumber() {
    return apartmentNumber;
  }

  public void setApartmentNumber(final long apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }
}
