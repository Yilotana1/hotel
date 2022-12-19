package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ApartmentIsNotAvailableException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.APARTMENT_NOT_AVAILABLE_PAGE;
import static com.example.hotel.controller.Path.CLIENT_HAS_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.SHOW_APPLY_PAGE;
import static com.example.hotel.controller.Path.SUCCESS_APPLY_PAGE;
import static com.example.hotel.model.dao.Tools.getLoginFromSession;
import static java.lang.Integer.parseInt;

public class ApplyCommand implements Command {
    public static final Logger log = Logger.getLogger(ApplyCommand.class);
    public static final String ERROR_ATTRIBUTE = "error" + SHOW_APPLY_PAGE;
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public ApplyCommand() {
    }

    public ApplyCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            ApplicationDTO.throwIfNotValid(request);
            final var applicationDTO = getApplicationDTOFromRequest(request);
            applicationService.apply(applicationDTO);
            request.getRequestDispatcher(SUCCESS_APPLY_PAGE).forward(request, response);

        } catch (final InvalidDataException e) {
            log.error("Validation error: " + e.getMessage());
            request.getSession().setAttribute(ERROR_ATTRIBUTE, e.getInvalidField() + "_is_invalid");
            redirectToApplyPage(request, response);
        } catch (final ClientHasNotCanceledApplicationException e) {
            log.error(e.getMessage(), e);
            request.getRequestDispatcher(CLIENT_HAS_APPLICATION_PAGE).forward(request, response);
        } catch (ApartmentIsNotAvailableException e) {
            log.error(e.getMessage(), e);
            request.getRequestDispatcher(APARTMENT_NOT_AVAILABLE_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            request.getRequestDispatcher(ERROR_503_PAGE).forward(request, response);
        }
    }

    private static ApplicationDTO getApplicationDTOFromRequest(final HttpServletRequest request) throws InvalidDataException {
        final var stayLength = request.getParameter("stay_length");
        final var apartmentNumber = request.getParameter("number");
        return ApplicationDTO.builder()
                .apartmentNumber(parseInt(apartmentNumber))
                .clientLogin(getLoginFromSession(request.getSession()))
                .stayLength(parseInt(stayLength))
                .build();
    }

    private static void redirectToApplyPage(final HttpServletRequest request, final HttpServletResponse response)throws IOException {
        final var number = request.getParameter("number");
        request.setAttribute("number", number);
        response.sendRedirect(request.getContextPath() + SHOW_APPLY_PAGE + "?number=" + number);

    }
}
