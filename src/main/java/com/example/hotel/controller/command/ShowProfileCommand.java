package com.example.hotel.controller.command;

import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.PROFILE_PAGE;

public class ShowProfileCommand implements Command {
    public final static Logger log = Logger.getLogger(ShowProfileCommand.class);
    public static final String LOGIN_INPUT = "login";
    public static final String USER_ATTRIBUTE = "user";
    public static final String APPLICATION_ATTRIBUTE = "application";

    private UserService userService = ServiceFactory.getInstance().createUserService();
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ShowProfileCommand() {
    }

    public ShowProfileCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws IOException{
        log.debug("Started command");
        final var login = (String) request.getSession().getAttribute(LOGIN_INPUT);
        try {
            final var user = userService
                    .getByLogin(login)
                    .orElseThrow(() -> new LoginIsNotFoundException(login + " is not found"));
            applicationService
                    .getNotApprovedApplicationByClientId(user.getId())
                    .ifPresent(app -> request.setAttribute(APPLICATION_ATTRIBUTE, app));
            request.setAttribute(USER_ATTRIBUTE, user);
            request.getRequestDispatcher(PROFILE_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }
}
