package com.example.hotel.model.dao;

import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
public class Tools {

    private static final int ID_INDEX = 1;
    private static final String LOGIN_ATTRIBUTE_NAME = "login";
    private static final String LOGGED_USERS_ATTRIBUTE = "loggedUsers";

    public static int getGeneratedId(final PreparedStatement statement) throws SQLException {
        final var keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(ID_INDEX);
    }

    public static boolean userIsLogged(final String login, final HttpServletRequest request) {
        final var savedLogins = (HashSet<String>) request.getServletContext().getAttribute("loggedUsers");
        return savedLogins.contains(login);
    }

    public static void addLoginToCache(final String login, final HttpServletRequest request) {
        final var loggedUsers = (HashSet<String>) request.getServletContext()
                .getAttribute("loggedUsers");
        loggedUsers.add(login);
    }
    public static void addRolesToSession(final Collection<Role> roles, final HttpServletRequest request) {
        final var session = request.getSession();
        session.setAttribute("roles", roles);
    }

    public static void addLoginToSession(final String login, final HttpServletRequest request) {
        final var session = request.getSession();
        session.setAttribute(LOGIN_ATTRIBUTE_NAME, login);
    }
    public static void addUserStatusToSession(final UserStatus userStatus, final HttpServletRequest request) {
        final var session = request.getSession();
        session.setAttribute("status", userStatus);
    }
    public static String getLoginFromSession(final HttpSession session) {
        return String.valueOf(session.getAttribute(LOGIN_ATTRIBUTE_NAME));
    }
    public static void deleteFromLoginCache(final HttpServletRequest request, final String login){
        final var loggedUsers = (HashSet<String>)request.getServletContext().getAttribute(LOGGED_USERS_ATTRIBUTE);
        loggedUsers.remove(login);
    }
}
