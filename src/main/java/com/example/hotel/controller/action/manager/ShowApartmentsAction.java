package com.example.hotel.controller.action.manager;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.action.Action;
import com.example.hotel.model.entity.Apartment;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static com.example.hotel.controller.commons.Tools.getPageId;

public class ShowApartmentsAction implements Action {
    public static final Logger log = Logger.getLogger(ShowApartmentsAction.class);
    public static final String RESIDENT_LOGIN = "resident_login";
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();
    private static final int PAGE_SIZE = 10;

    public ShowApartmentsAction() {
    }

    public ShowApartmentsAction(final ServiceFactory serviceFactory) {
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
                setApartmentsToRequestScope(request, apartmentsMap);
                final var totalPagesNumber = apartmentService.count() / PAGE_SIZE;
                setFinalAttributes(request, pageId, totalPagesNumber);
                request.getRequestDispatcher(Path.Get.Manager.APARTMENTS_PAGE).forward(request, response);
                return;
            }
            final var apartment = apartmentService.getApartmentByResidentLogin(login);
            apartment.ifPresent(a -> setApartmentsToRequestScope(request, Map.of(a, login)));
            final var totalApartmentsNumber = apartment.map(a -> 1).orElse(0);
            setFinalAttributes(request, pageId, totalApartmentsNumber);
            request.getRequestDispatcher(Path.Get.Manager.APARTMENTS_PAGE).forward(request, response);

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private static void setApartmentsToRequestScope(final HttpServletRequest request,
                                                    final Map<Apartment, String> apartmentsMap) {
        request.setAttribute(RequestAttributes.APARTMENTS, apartmentsMap);
    }

    private void setFinalAttributes(final HttpServletRequest request,
                                    final int pageId,
                                    final Integer totalApartmentsNumber) {
        request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalApartmentsNumber);
        request.setAttribute(PaginationAttributes.PAGE, pageId);

    }
}
