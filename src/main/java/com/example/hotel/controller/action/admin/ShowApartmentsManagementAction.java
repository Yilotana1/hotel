package com.example.hotel.controller.action.admin;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.action.Action;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ShowApartmentsManagementAction implements Action {
    public static final Logger log = Logger.getLogger(ShowApartmentsManagementAction.class);
    public static final String RESIDENT_LOGIN = "resident_login";
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();
    private static final int PAGE_SIZE = 7;
    public ShowApartmentsManagementAction() {
    }

    public ShowApartmentsManagementAction(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageId = getPageId(request);
            final var skip = (pageId - 1) * PAGE_SIZE;
            final var login = request.getParameter(RESIDENT_LOGIN);
            if (login == null || login.isEmpty()) {
                final var apartmentsMap = apartmentService.getApartmentsWithResidentLogins(skip, PAGE_SIZE);
                request.setAttribute(RequestAttributes.APARTMENTS, apartmentsMap);
                final var totalPagesNumber = apartmentService.count() / PAGE_SIZE;
                setFinalAttributes(request, pageId, totalPagesNumber);
                request.getRequestDispatcher(Path.Get.Admin.APARTMENT_MANAGEMENT_PAGE).forward(request, response);
                return;
            }
            final var apartment = apartmentService.getApartmentByResidentLogin(login);
            apartment.ifPresent(a -> request.setAttribute(RequestAttributes.APARTMENTS, Map.of(a, login)));
            final var totalPagesNumber = apartment.map(a -> 1).orElse(0);
            setFinalAttributes(request, pageId, totalPagesNumber);
            request.getRequestDispatcher(Path.Get.Admin.APARTMENT_MANAGEMENT_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }



    private void setFinalAttributes(final HttpServletRequest request,
                                    final int pageId,
                                    final int totalPagesNumber) {
        request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalPagesNumber);
        request.setAttribute(PaginationAttributes.PAGE, pageId);
    }

    private int getPageId(final HttpServletRequest request) {
        final var pageInput = request.getParameter(PaginationAttributes.PAGE);
        final var page = requireNonNullElse(pageInput, PaginationAttributes.DEFAULT_PAGE);
        return parseInt(page);
    }
}
