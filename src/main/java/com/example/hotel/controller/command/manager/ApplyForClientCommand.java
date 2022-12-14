package com.example.hotel.controller.command.manager;

import com.example.hotel.commons.Constants.RequestParameters;
import com.example.hotel.commons.Path;
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
            final var redirectPath = request.getContextPath()
                    + Path.Get.Manager.SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE;
            response.sendRedirect(redirectPath);

        } catch (final ClientHasNotCanceledApplicationException e) {
            log.error(e.getMessage(), e);
            final var redirectPath = request.getContextPath() + Path.Get.Manager.CLIENT_HAS_ASSIGNED_APPLICATION_PAGE;
            response.sendRedirect(redirectPath);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    public static ApplicationDTO getApplicationDTOFromRequest(final HttpServletRequest request) throws InvalidDataException {
        final var stayLength = request.getParameter(RequestParameters.STAY_LENGTH);
        if (stayLength == null || stayLength.isBlank()) {
            throw new InvalidDataException("stayLength must be specified", RequestParameters.STAY_LENGTH);
        }
        final var apartmentNumber = request.getParameter(RequestParameters.APARTMENT_NUMBER);
        if (apartmentNumber == null) {
            throw new InvalidDataException("apartmentNumber must be specified", RequestParameters.STAY_LENGTH);
        }
        return ApplicationDTO.builder()
                .apartmentNumber(parseInt(apartmentNumber))
                .clientLogin(request.getParameter(RequestParameters.LOGIN))
                .stayLength(parseInt(stayLength))
                .build();
    }
}
