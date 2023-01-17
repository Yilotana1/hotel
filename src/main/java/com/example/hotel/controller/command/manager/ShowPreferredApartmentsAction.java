package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.command.Action;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collection;

import static com.example.hotel.controller.commons.Tools.getPageId;

public class ShowPreferredApartmentsAction implements Action {
    public static final Logger log = Logger.getLogger(ShowPreferredApartmentsAction.class);
    private static final int PAGE_SIZE = 10;
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();

    public ShowPreferredApartmentsAction() {
    }

    public ShowPreferredApartmentsAction(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var clientLogin = request.getParameter(RequestAttributes.LOGIN);
            final var pageNumber = getPageId(request);
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            final var apartments = apartmentService.getPreferredApartments(clientLogin, skip, PAGE_SIZE);
            setApartmentsAttributesToRequestScope(request, clientLogin, apartments);
            final var apartmentsCount = apartmentService.preferredApartmentsCount(clientLogin);

            request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, apartmentsCount / PAGE_SIZE);
            request.getRequestDispatcher(Path.Get.Manager.PREFERRED_APARTMENTS_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private static void setApartmentsAttributesToRequestScope(final HttpServletRequest request,
                                                              final String clientLogin,
                                                              final Collection<Apartment> apartments) {
        request.setAttribute(RequestAttributes.APARTMENTS, apartments);
        request.setAttribute(RequestAttributes.STAY_LENGTH, request.getParameter(RequestParameters.STAY_LENGTH));
        request.setAttribute(RequestAttributes.LOGIN, clientLogin);
    }
}
