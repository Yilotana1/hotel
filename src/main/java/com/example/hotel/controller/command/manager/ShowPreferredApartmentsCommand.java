package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.Get.Manager.PREFERRED_APARTMENTS_PAGE;
import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ShowPreferredApartmentsCommand implements Command {
    public static final Logger log = Logger.getLogger(ShowPreferredApartmentsCommand.class);

    private static final String PAGE_NUMBER_INPUT = "page";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final int PAGE_SIZE = 10;
    private static final String TOTAL_PAGES_NUMBER = "count";
    public static final String CLIENT_LOGIN_PARAMETER = "client_login";
    public static final String APARTMENTS_ATTRIBUTE = "apartments";
    public static final String STAY_LENGTH_ATTRIBUTE = "stay_length";

    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();

    public ShowPreferredApartmentsCommand() {
    }

    public ShowPreferredApartmentsCommand(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var clientLogin = request.getParameter(CLIENT_LOGIN_PARAMETER);
            final var pageNumber = parseInt(
                    requireNonNullElse(request.getParameter(PAGE_NUMBER_INPUT), DEFAULT_PAGE_NUMBER));
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            final var apartments = apartmentService
                    .getPreferredApartments(clientLogin, skip, PAGE_SIZE);
            request.setAttribute(APARTMENTS_ATTRIBUTE, apartments);
            request.setAttribute(STAY_LENGTH_ATTRIBUTE, request.getParameter(STAY_LENGTH_ATTRIBUTE));
            request.setAttribute(CLIENT_LOGIN_PARAMETER, request.getParameter(CLIENT_LOGIN_PARAMETER));
            final var apartmentsCount = apartmentService.preferredApartmentsCount(clientLogin);
            request.setAttribute(TOTAL_PAGES_NUMBER, apartmentsCount / PAGE_SIZE);
            request.getRequestDispatcher(PREFERRED_APARTMENTS_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }
}
