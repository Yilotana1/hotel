package com.example.hotel.controller.command;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotFoundException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ShowProfileAction implements Action {
    public final static Logger log = Logger.getLogger(ShowProfileAction.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ShowProfileAction() {
    }

    public ShowProfileAction(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        log.debug("Started command");
        final var login = (String) request.getSession().getAttribute(SessionAttributes.LOGIN);
        try {
            final var user = userService
                    .getByLogin(login)
                    .orElseThrow(() -> new LoginIsNotFoundException(login + " is not found"));
            setNotApprovedApplicationToRequestScope(request, user);
            request.setAttribute(RequestAttributes.USER, user);
            request.getRequestDispatcher(Path.Get.User.PROFILE_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private void setNotApprovedApplicationToRequestScope(final HttpServletRequest request,
                                                         final User user) {
        applicationService
                .getNotApprovedApplicationByClientId(user.getId())
                .ifPresent(app -> request.setAttribute(RequestAttributes.APPLICATION, app));
    }
}
