package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.CLIENT_HAS_ASSIGNED_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE;
import static java.lang.Integer.parseInt;

public class ApplyForClientCommand implements Command {
    public static final Logger log = Logger.getLogger(ApplyForClientCommand.class);
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ApplyForClientCommand() {
    }

    public ApplyForClientCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var applicationDTO = getApplicationDTOFromRequest(request);
            applicationService.apply(applicationDTO);
            response.sendRedirect(request.getContextPath() + SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE);
        } catch (final ClientHasNotCanceledApplicationException e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + CLIENT_HAS_ASSIGNED_APPLICATION_PAGE);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }

    public static ApplicationDTO getApplicationDTOFromRequest(final HttpServletRequest request) throws InvalidDataException {
        final var stayLength = request.getParameter("stay_length");
        if (stayLength == null || stayLength.isBlank()) {
            throw new InvalidDataException("stayLength must be specified", "stay_length");
        }
        final var apartmentNumber = request.getParameter("number");
        if (apartmentNumber == null) {
            throw new InvalidDataException("apartmentNumber must be specified", "stay_length");
        }
        return ApplicationDTO.builder()
                .apartmentNumber(parseInt(apartmentNumber))
                .clientLogin(request.getParameter("client_login"))
                .stayLength(parseInt(stayLength))
                .build();
    }
}
