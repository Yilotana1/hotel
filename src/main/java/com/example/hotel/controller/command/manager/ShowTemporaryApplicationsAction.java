package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.command.Action;
import com.example.hotel.model.entity.TemporaryApplication;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.example.hotel.controller.commons.Tools.getPageId;

public class ShowTemporaryApplicationsAction implements Action {
    public static final Logger log = Logger.getLogger(ShowPreferredApartmentsAction.class);
    private static final int PAGE_SIZE = 10;
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ShowTemporaryApplicationsAction() {
    }

    public ShowTemporaryApplicationsAction(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageNumber = getPageId(request);
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            final var clientLogin = request.getParameter(RequestParameters.LOGIN);

            if (clientLogin == null || clientLogin.isEmpty()) {
                final var temporaryApplications = applicationService.getTemporaryApplications(skip, PAGE_SIZE);
                setTemporaryApplicationsAttribute(request, temporaryApplications);
                final var totalPagesNumber = applicationService.temporaryApplicationsCount() / PAGE_SIZE;
                request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalPagesNumber);
                forwardToTemporaryApplicationsPage(request, response);
                return;

            }
            final var temporaryApplication = applicationService.getTemporaryApplicationByLogin(clientLogin);
            temporaryApplication
                    .ifPresent(tempApp -> setTemporaryApplicationsAttribute(request, List.of(tempApp)));
            final var totalPagesNumber = temporaryApplication.map(tempApp -> 1).orElse(0);
            request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalPagesNumber);
            forwardToTemporaryApplicationsPage(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private static void forwardToTemporaryApplicationsPage(final HttpServletRequest request,
                                                           final HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(Path.Get.Manager.TEMPORARY_APPLICATIONS_PAGE).forward(request, response);
    }

    private void setTemporaryApplicationsAttribute(final HttpServletRequest request,
                                                   final Collection<TemporaryApplication> temporaryApplications) {
        request.setAttribute(RequestAttributes.TEMPORARY_APPLICATIONS, temporaryApplications);
    }
}
