package com.example.hotel.model.entity;

import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import static com.example.hotel.model.entity.enums.Role.CLIENT;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;
import static java.math.BigDecimal.ZERO;

public class User {
    public final static Logger log = Logger.getLogger(User.class);
    private Long id;
    private String login;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phone;
    private UserStatus status = NON_BLOCKED;
    private Collection<Role> roles = new HashSet<>();
    private BigDecimal money = ZERO;

    private User() {
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", roles=" + roles +
                ", money=" + money +
                '}';
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private final User user = new User();

        public User build() {
            return user;
        }

        public UserBuilder id(Long id) {
            user.setId(id);
            return this;
        }

        public UserBuilder status(UserStatus status) {
            user.setStatus(status);
            return this;
        }

        public UserBuilder roles(Collection<Role> roles) {
            user.setRoles(roles);
            return this;
        }

        public UserBuilder login(String login) {
            user.setLogin(login);
            return this;
        }

        public UserBuilder firstname(String firstname) {
            user.setFirstname(firstname);
            return this;
        }

        public UserBuilder lastname(String lastname) {
            user.setLastname(lastname);
            return this;
        }

        public UserBuilder email(String email) {
            user.setEmail(email);
            return this;
        }

        public UserBuilder password(String password) {
            user.setPassword(password);
            return this;
        }

        public UserBuilder phone(String phone) {
            user.setPhone(phone);
            return this;
        }

        public UserBuilder money(BigDecimal money) {
            user.setMoney(money);
            return this;
        }

    }

    public void payForApartment(final Application application){
        log.trace(login + ": paid applicationPrice. Price = " + application.getPrice());
        setMoney(money.subtract(application.getPrice()));
    }

    public void updateMoneyAccount(final BigDecimal money) {
        log.trace(login + ": money got updated. Money = " + money);
        setMoney(this.money.add(money));
    }

    public void assignAsClient() {
        roles.add(CLIENT);
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
