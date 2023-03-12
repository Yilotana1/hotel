package com.example.hotel.controller.dto;

import org.apache.log4j.Logger;

public class UserDTO {
  public final static Logger log = Logger.getLogger(UserDTO.class);
  private String login;
  private String firstname;
  private String lastname;
  private String email;
  private String password;
  private String phone;

  private UserDTO() {
  }

  public static UserDTOBuilder builder() {
    return new UserDTOBuilder();
  }

  public static class UserDTOBuilder {
    private final UserDTO userDTO = new UserDTO();

    public UserDTO build() {
      return userDTO;
    }

    public UserDTO.UserDTOBuilder login(final String login) {
      userDTO.setLogin(login);
      return this;
    }

    public UserDTO.UserDTOBuilder firstname(final String firstname) {
      userDTO.setFirstname(firstname);
      return this;
    }

    public UserDTO.UserDTOBuilder lastname(final String lastname) {
      userDTO.setLastname(lastname);
      return this;
    }

    public UserDTO.UserDTOBuilder email(final String email) {
      userDTO.setEmail(email);
      return this;
    }

    public UserDTO.UserDTOBuilder password(final String password) {
      userDTO.setPassword(password);
      return this;
    }

    public UserDTO.UserDTOBuilder phone(final String phone) {
      userDTO.setPhone(phone);
      return this;
    }
  }


  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }
}
