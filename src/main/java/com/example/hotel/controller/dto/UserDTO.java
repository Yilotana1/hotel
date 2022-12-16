package com.example.hotel.controller.dto;

import com.example.hotel.controller.exception.InvalidDataException;

import static java.util.Objects.requireNonNull;

public class UserDTO {
    private static final int MAX_LOGIN_LENGTH = 16;
    private static final int MAX_FIRSTNAME_LENGTH = 20;
    private static final int MAX_LASTNAME_LENGTH = 20;
    private static final int MAX_PHONE_LENGTH = 45;
    public static final String EMAIL_REGEX = "^(.+)@(.+)$";
    public static final String FIRSTNAME_REGEX = "^[\\p{IsCyrillic}A-Za-z]+$";
    public static final String LASTNAME_REGEX = "^[\\p{IsCyrillic}A-Za-z]+$";
    public static final String PHONE_REGEX = "^\\+\\d+$";
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 32;
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

        public UserDTO.UserDTOBuilder login(String login) {
            userDTO.setLogin(login);
            return this;
        }

        public UserDTO.UserDTOBuilder firstname(String firstname) {
            userDTO.setFirstname(firstname);
            return this;
        }

        public UserDTO.UserDTOBuilder lastname(String lastname) {
            userDTO.setLastname(lastname);
            return this;
        }

        public UserDTO.UserDTOBuilder email(String email) {
            userDTO.setEmail(email);
            return this;
        }

        public UserDTO.UserDTOBuilder password(String password) {
            userDTO.setPassword(password);
            return this;
        }

        public UserDTO.UserDTOBuilder phone(String phone) {
            userDTO.setPhone(phone);
            return this;
        }
    }

    public void throwIfNotValid() throws InvalidDataException {
        if (hasNotAllowedNulls()) {
            throw new InvalidDataException("Some of user dto fields are null");
        }
        if (login.length() > MAX_LOGIN_LENGTH || login.isBlank()) {
            throw new InvalidDataException("Login must be of appropriate size", "login");
        }
        if (firstname.length() > MAX_FIRSTNAME_LENGTH || !firstname.matches(FIRSTNAME_REGEX)) {
            throw new InvalidDataException("Firstname must not be too long and must only have letters", "firstname");
        }
        if (lastname.length() > MAX_LASTNAME_LENGTH || !lastname.matches(LASTNAME_REGEX)) {
            throw new InvalidDataException("Lastname must not be too long and must only have letters", "lastname");
        }
        if ((email != null && email.isEmpty()) || (email != null && !email.matches(EMAIL_REGEX))) {
            throw new InvalidDataException("Email must follow typical pattern", "email");
        }
        if (phone.length() > MAX_PHONE_LENGTH || !phone.matches(PHONE_REGEX)) {
            throw new InvalidDataException("Phone must only have digits and must not be too long", "phone");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new InvalidDataException("Password is incorrect", "password");
        }
    }

    private boolean hasNotAllowedNulls() throws InvalidDataException {
        try {
            requireNonNull(login);
            requireNonNull(firstname);
            requireNonNull(lastname);
            requireNonNull(phone);
            requireNonNull(password);
        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }

//    public BigDecimal getMoney() {
//        return money;
//    }
//
//    public void setMoney(BigDecimal money) {
//        this.money = money;
//    }

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
