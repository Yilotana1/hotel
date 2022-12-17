package com.example.hotel.controller.dto;

import com.example.hotel.controller.exception.InvalidDataException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import static java.util.Objects.requireNonNull;

public class UserDTO {
    public final static Logger log = Logger.getLogger(UserDTO.class);
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

    public static void throwIfNotValid(final HttpServletRequest request) throws InvalidDataException {
        final var login = request.getParameter("login");
        final var firstname = request.getParameter("firstname");
        final var lastname = request.getParameter("lastname");
        final var email = request.getParameter("email");
        final var password = request.getParameter("password");
        final var phone = request.getParameter("phone");
        throwIfNulls(login, firstname,
                lastname, email,
                password, phone);
        if (login.length() > MAX_LOGIN_LENGTH || login.isEmpty()) {
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

    private static void throwIfNulls(final String... args) throws InvalidDataException{
        try {
            for (final var arg : args) {
                requireNonNull(arg);
            }
        } catch (final NullPointerException e) {
            final var message = "Some of the required parameters for UserDTO are missing.";
            log.error(message, e);
            throw new InvalidDataException(message);
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
