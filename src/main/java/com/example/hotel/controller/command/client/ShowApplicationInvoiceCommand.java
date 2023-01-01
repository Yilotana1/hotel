package com.example.hotel.controller.command.client;

import com.example.hotel.commons.Constants.RequestAttributes;
import com.example.hotel.commons.Path;
import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.exception.ApplicationNotFoundException;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.commons.Tools.getLoginFromSession;
import static java.lang.String.format;

public class ShowApplicationInvoiceCommand implements Command {
    public final static Logger log = Logger.getLogger(ShowApplicationInvoiceCommand.class);
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ShowApplicationInvoiceCommand() {
    }

    public ShowApplicationInvoiceCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var login = getLoginFromSession(request.getSession());
            final var application = getApplicationFromService(login);

            request.setAttribute(RequestAttributes.APPLICATION, application);
            request.getRequestDispatcher(Path.Get.Client.APPLICATION_INVOICE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private Application getApplicationFromService(final String login) {
        return applicationService
                .getApplicationToConfirm(login)
                .orElseThrow(() -> new ApplicationNotFoundException(
                        format("Application of client with login - %s not found", login)));
    }
}
