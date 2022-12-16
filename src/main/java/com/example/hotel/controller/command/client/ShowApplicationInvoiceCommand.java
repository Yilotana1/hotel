package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.exception.ApplicationNotFoundException;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.APPLICATION_INVOICE_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.model.dao.Tools.getLoginFromSession;
import static java.lang.String.format;

public class ShowApplicationInvoiceCommand implements Command {
    public final static Logger log = Logger.getLogger(ShowApplicationInvoiceCommand.class);
    public static final String APPLICATION_NOT_FOND_MESSAGE = "Application of client with login - %s not found";
    public static final String APPLICATION_ATTRIBUTE = "application";
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
            final var application = applicationService
                    .getApplicationToConfirm(login)
                    .orElseThrow(() -> new ApplicationNotFoundException(format(APPLICATION_NOT_FOND_MESSAGE, login)));
            request.setAttribute(APPLICATION_ATTRIBUTE, application);
            request.getRequestDispatcher(APPLICATION_INVOICE_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }
}
