package com.example.hotel.controller.command;

import com.example.hotel.commons.Constants.RequestAttributes;
import com.example.hotel.commons.Constants.RequestParameters;
import com.example.hotel.commons.Path;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.commons.Tools.addLoginToCache;
import static com.example.hotel.commons.Tools.addLoginToSession;
import static com.example.hotel.commons.Tools.addRolesToSession;
import static com.example.hotel.commons.Tools.addUserStatusToSession;
import static com.example.hotel.commons.Tools.userIsLogged;

public class LoginCommand implements Command {
    public final static Logger log = Logger.getLogger(LoginCommand.class);
    public final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.User.LOGIN_PAGE;
    private UserService userService = ServiceFactory.getInstance().createUserService();

    public LoginCommand() {
    }

    public LoginCommand(ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            log.trace("Started Login command");
            final var login = request.getParameter(RequestParameters.LOGIN);
            final var password = request.getParameter(RequestParameters.PASSWORD);
            if (credentialsNotSpecified(login, password)) {
                processLoginError(request, response, "credentials_not_specified");
                return;
            }
            if (userIsLogged(login, request)) {
                processLoginError(request, response, "user_in_system");
                return;
            }
            final var user = userService.signIn(login, password);
            if (user.isEmpty()) {
                log.error("User login-process failed");
                processLoginError(request, response, "user_not_found");
                return;
            }
            addUsersDataIntoContext(request, login, user.get());
            response.sendRedirect(request.getContextPath() + Path.Get.User.PROFILE);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private boolean credentialsNotSpecified(final String login, final String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            log.error("Login or password are not specified");
            return true;
        }
        return false;
    }

    private void processLoginError(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final String errorMessageProperty) throws IOException {
        request.getSession().setAttribute(ERROR_ATTRIBUTE, errorMessageProperty);
        response.sendRedirect(request.getContextPath() + Path.Get.User.LOGIN_PAGE);
    }

    private void addUsersDataIntoContext(final HttpServletRequest request,
                                         final String login,
                                         final User user) {
        addLoginToCache(login, request);
        addRolesToSession(user.getRoles(), request);
        addLoginToSession(login, request);
        addUserStatusToSession(user.getStatus(), request);
        log.trace(login + " is logged");
    }
}
