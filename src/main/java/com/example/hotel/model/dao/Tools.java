package com.example.hotel.model.dao;

import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
public class Tools {

    private static final int ID_INDEX = 1;
    private static final String LOGIN_ATTRIBUTE_NAME = "login";
    private static final String LOGGED_USERS_ATTRIBUTE = "loggedUsers";

    public static int getGeneratedId(PreparedStatement statement) throws SQLException {
        ResultSet keys = statement.getGeneratedKeys();
        keys.next();
        return keys.getInt(ID_INDEX);
    }

    public static boolean userIsLogged(String login, HttpServletRequest request) {
        var savedLogins = (HashSet<String>) request.getServletContext().getAttribute("loggedUsers");
        return savedLogins.contains(login);
    }

    public static void addLoginToCache(final String login, final HttpServletRequest request) {
        var loggedUsers = (HashSet<String>) request.getServletContext()
                .getAttribute("loggedUsers");
        loggedUsers.add(login);
    }
    public static void addRolesToSession(Collection<Role> roles, HttpServletRequest request) {
        var session = request.getSession();
        session.setAttribute("roles", roles);
    }

    public static void addLoginToSession(String login, HttpServletRequest request) {
        var session = request.getSession();
        session.setAttribute(LOGIN_ATTRIBUTE_NAME, login);
    }
    public static void addUserStatusToSession(UserStatus userStatus, HttpServletRequest request) {
        var session = request.getSession();
        session.setAttribute("status", userStatus);
    }
    public static String getLoginFromSession(HttpSession session) {
        return String.valueOf(session.getAttribute(LOGIN_ATTRIBUTE_NAME));
    }
    public static void deleteFromLoginCache(HttpServletRequest request, String login){
        var loggedUsers = (HashSet<String>)request.getServletContext().getAttribute(LOGGED_USERS_ATTRIBUTE);
        loggedUsers.remove(login);
    }
}
