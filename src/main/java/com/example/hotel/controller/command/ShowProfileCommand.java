package com.example.hotel.controller.command;

import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.PROFILE_PAGE;

public class ShowProfileCommand implements Command {
    public final static Logger log = Logger.getLogger(ShowProfileCommand.class);
    public static final String LOGIN_INPUT = "login";
    public static final String USER_ATRRIBUTE = "user";

    private UserService userService = ServiceFactory.getInstance().createUserService();

    public ShowProfileCommand() {
    }

    public ShowProfileCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        log.debug("Started command");
        final var login = (String) request.getSession().getAttribute(LOGIN_INPUT);
        try {
            final var user = userService
                    .getByLogin(login)
                    .orElseThrow(() -> new LoginIsNotFoundException(login + " is not found"));

            request.setAttribute(USER_ATRRIBUTE, user);
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } catch (LoginIsNotFoundException e) {
            log.error(e.getMessage());
            request.getRequestDispatcher(ERROR_503_PAGE).forward(request, response);
        }

    }
}
