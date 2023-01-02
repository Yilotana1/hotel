package com.example.hotel.commons;

import com.example.hotel.commons.Constants.RequestAttributes;
import com.example.hotel.commons.Constants.ServletContextAttributes;
import com.example.hotel.commons.Constants.SessionAttributes;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.entity.enums.UserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.HashSet;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class Tools {
    public final static Logger log = Logger.getLogger(Tools.class);

    public static void setInvalidFieldMessage(final HttpServletRequest request,
                                              final InvalidDataException e,
                                              final String errorAttribute) {
        final var message = e.getInvalidField() + SessionAttributes.INVALID_SUFFIX;
        request
                .getSession()
                .setAttribute(errorAttribute, message);
    }

    public static int getPageId(final HttpServletRequest request) {
        final var pageInput = request.getParameter(RequestAttributes.PaginationAttributes.PAGE);
        final var page = requireNonNullElse(pageInput, RequestAttributes.PaginationAttributes.DEFAULT_PAGE);
        return parseInt(page);
    }

    public static boolean userIsLogged(final String login, final HttpServletRequest request) {
        final var savedLogins =
                (HashSet<String>) request
                        .getServletContext()
                        .getAttribute(ServletContextAttributes.LOGGED_USERS);
        if (savedLogins.contains(login)) {
            final var logMessage = "User has already been logged";
            log.error(logMessage);
            return true;
        }
        return false;
    }

    public static void addLoginToCache(final String login, final HttpServletRequest request) {
        final var loggedUsers = (HashSet<String>) request.getServletContext()
                .getAttribute(ServletContextAttributes.LOGGED_USERS);
        loggedUsers.add(login);
    }

    public static void addRolesToSession(final Collection<Role> roles, final HttpServletRequest request) {
        final var session = request.getSession();
        session.setAttribute(SessionAttributes.ROLES, roles);
    }

    public static void addLoginToSession(final String login, final HttpServletRequest request) {
        final var session = request.getSession();
        session.setAttribute(SessionAttributes.LOGIN, login);
    }

    public static void addUserStatusToSession(final UserStatus userStatus, final HttpServletRequest request) {
        final var session = request.getSession();
        session.setAttribute(SessionAttributes.STATUS, userStatus);
    }

    public static String getLoginFromSession(final HttpSession session) {
        return String.valueOf(session.getAttribute(SessionAttributes.LOGIN));
    }

    public static void deleteFromLoginCache(final HttpServletRequest request, final String login) {
        final var loggedUsers =
                (HashSet<String>) request
                        .getServletContext()
                        .getAttribute(ServletContextAttributes.LOGGED_USERS);
        loggedUsers.remove(login);
    }
}
