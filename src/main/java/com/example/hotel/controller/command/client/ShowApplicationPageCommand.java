package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.APARTMENT_NOT_AVAILABLE_PAGE;
import static com.example.hotel.controller.Path.CLIENT_APPLY_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static java.lang.String.format;

public class ShowApplicationPageCommand implements Command {

    public final static Logger log = Logger.getLogger(ShowApplicationPageCommand.class);
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();

    public ShowApplicationPageCommand() {
    }

    public ShowApplicationPageCommand(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var apartmentNumber = Long.parseLong(request.getParameter("number"));
            final var apartment = apartmentService.getByNumber(apartmentNumber);
            if (apartment.isEmpty() || apartment.get().isNotAvailable()) {
                log.error(format("Request apartment(number = %d) not available", apartmentNumber));
                response.sendRedirect(request.getContextPath() + APARTMENT_NOT_AVAILABLE_PAGE);
                return;
            }
            request.setAttribute("apartment", apartment.get());
            request.getRequestDispatcher(CLIENT_APPLY_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e);
            request.getRequestDispatcher(ERROR_503_PAGE).forward(request, response);
        }
    }
}
