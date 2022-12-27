package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static com.example.hotel.controller.Path.Get.Manager.APARTMENTS_PAGE;
import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ShowApartmentsCommand implements Command {
    public static final Logger log = Logger.getLogger(ShowApartmentsCommand.class);
    public static final String RESIDENT_LOGIN = "resident_login";

    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();
    private static final String PAGE_NUMBER_INPUT = "page";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final int PAGE_SIZE = 10;
    private static final String TOTAL_PAGES_NUMBER = "count";

    private static final String APARTMENTS = "apartments";

    public ShowApartmentsCommand() {
    }

    public ShowApartmentsCommand(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }
    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageNumber = parseInt(requireNonNullElse(request.getParameter(PAGE_NUMBER_INPUT), DEFAULT_PAGE_NUMBER));
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            final var login = request.getParameter(RESIDENT_LOGIN);
            if (login != null && !login.isEmpty()) {
                final var apartment = apartmentService.getApartmentByResidentLogin(login);
                apartment.ifPresent(a -> request.setAttribute(APARTMENTS, Map.of(a, login)));
                request.setAttribute(TOTAL_PAGES_NUMBER, apartment.map(a -> 1).orElse(0));
            } else {
                final var apartmentsMap = apartmentService.getApartmentsWithResidentLogins(skip, PAGE_SIZE);
                request.setAttribute(APARTMENTS, apartmentsMap);
                final var totalPagesNumber = apartmentService.count() / PAGE_SIZE;
                request.setAttribute(TOTAL_PAGES_NUMBER, totalPagesNumber);
            }
            request.setAttribute(PAGE_NUMBER_INPUT, pageNumber);
            request.getRequestDispatcher(APARTMENTS_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }
}
