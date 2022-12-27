package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.example.hotel.controller.Path.Get.Manager.TEMPORARY_APPLICATIONS_PAGE;
import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ShowTemporaryApplicationsCommand implements Command {
    public static final Logger log = Logger.getLogger(ShowPreferredApartmentsCommand.class);

    private static final String PAGE_NUMBER_INPUT = "page";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final int PAGE_SIZE = 10;
    public static final String TEMPORARY_APPLICATIONS = "temporary_applications";
    private static final String TOTAL_PAGES_NUMBER = "count";
    public static final String CLIENT_LOGIN_PARAMETER = "client_login";

    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ShowTemporaryApplicationsCommand() {
    }

    public ShowTemporaryApplicationsCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageNumber = parseInt(
                    requireNonNullElse(request.getParameter(PAGE_NUMBER_INPUT), DEFAULT_PAGE_NUMBER));
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            final var clientLogin = request.getParameter(CLIENT_LOGIN_PARAMETER);
            if (clientLogin != null && !clientLogin.isEmpty()) {
                final var temporaryApplication = applicationService.getTemporaryApplicationByLogin(clientLogin);
                temporaryApplication
                        .ifPresent(tempApp -> request.setAttribute(TEMPORARY_APPLICATIONS, List.of(tempApp)));
                request.setAttribute(TOTAL_PAGES_NUMBER, temporaryApplication.map(tempApp -> 1).orElse(0));
            } else {
                final var temporaryApplications = applicationService.getTemporaryApplications(skip, PAGE_SIZE);
                request.setAttribute(TEMPORARY_APPLICATIONS, temporaryApplications);
                final var totalPagesNumber = applicationService.temporaryApplicationsCount() / PAGE_SIZE;
                request.setAttribute(TOTAL_PAGES_NUMBER, totalPagesNumber);
            }
            request.getRequestDispatcher(TEMPORARY_APPLICATIONS_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }
}
