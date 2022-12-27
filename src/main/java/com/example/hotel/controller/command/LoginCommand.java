package com.example.hotel.controller.command;

import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.Get.User.LOGIN_PAGE;
import static com.example.hotel.controller.Path.Get.User.PROFILE;
import static com.example.hotel.model.dao.Tools.addLoginToCache;
import static com.example.hotel.model.dao.Tools.addLoginToSession;
import static com.example.hotel.model.dao.Tools.addRolesToSession;
import static com.example.hotel.model.dao.Tools.addUserStatusToSession;
import static com.example.hotel.model.dao.Tools.userIsLogged;

public class LoginCommand implements Command {
    public final static Logger log = Logger.getLogger(LoginCommand.class);
    public static final String ERROR_ATTRIBUTE = "error" + LOGIN_PAGE;

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
            final var login = request.getParameter("login");
            final var password = request.getParameter("password");
            if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
                log.error("Login or password are not specified");
                request.getSession().setAttribute(ERROR_ATTRIBUTE, "credentials_not_specified");
                response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
                return;
            }
            if (userIsLogged(login, request)) {
                log.error("User has already been logged");
                request.getSession().setAttribute(ERROR_ATTRIBUTE, "user_in_system");
                response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
                return;
            }
            final var user = userService.signIn(login, password);
            if (user.isEmpty()) {
                log.error("User login-process has failed");
                request.getSession().setAttribute(ERROR_ATTRIBUTE, "user_not_found");
                response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
                return;
            }
            addLoginToCache(login, request);
            addRolesToSession(user.get().getRoles(), request);
            addLoginToSession(login, request);
            addUserStatusToSession(user.get().getStatus(), request);
            log.trace(login + " is logged");
            response.sendRedirect(request.getContextPath() + PROFILE);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }
}
