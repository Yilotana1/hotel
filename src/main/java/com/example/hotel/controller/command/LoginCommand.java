package com.example.hotel.controller.command;

import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.LOGIN_PAGE;
import static com.example.hotel.controller.Path.PROFILE;
import static com.example.hotel.model.dao.Tools.addLoginToSession;
import static com.example.hotel.model.dao.Tools.addRolesToSession;
import static com.example.hotel.model.dao.Tools.addUserStatusToSession;
import static com.example.hotel.model.dao.Tools.addloginToLoginCache;
import static com.example.hotel.model.dao.Tools.userIsLogged;

public class LoginCommand implements Command {
    public final static Logger log = Logger.getLogger(LoginCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.trace("Started Login command");
        var login = request.getParameter("login");
        var password = request.getParameter("password");

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            log.error("Login or password are not specified");
            request.setAttribute("error", "credentials_not_specified");
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            return;
        }
        if (userIsLogged(login, request)) {
            log.error("User has already been logged");
            request.setAttribute("error", "user_in_system");
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            return;
        }

        var userService = ServiceFactory.getInstance().createUserService();
        var user = userService.signIn(login, password);
        if (user.isEmpty()) {
            log.error("User login-process has failed");
            request.setAttribute("error", "user_not_found");
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            return;
        }
        addloginToLoginCache(login, request);
        addRolesToSession(user.get().getRoles(), request);
        addLoginToSession(login, request);
        addUserStatusToSession(user.get().getStatus(), request);
        log.trace("Added user to login cache: login = " + login);
        log.trace("Roles and login have been added to session");
        log.trace(login + " is logged");
        response.sendRedirect(request.getContextPath() + PROFILE);
    }
}
