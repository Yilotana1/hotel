package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ApartmentsNotFoundException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.CLIENT_HAS_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.MAKE_TEMPORARY_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.SUCCESS_APPLY_PAGE;
import static com.example.hotel.model.dao.Tools.getLoginFromSession;
import static java.lang.Integer.parseInt;

public class MakeTemporaryApplicationCommand implements Command {
    public final static Logger log = Logger.getLogger(MakeTemporaryApplicationCommand.class);
    public static final String ERROR_PROPERTY_NAME = "apartments_with_such_parameters_dont_exist";
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

    public MakeTemporaryApplicationCommand() {
    }

    public MakeTemporaryApplicationCommand(final ServiceFactory serviceFactory) {
        applicationService = serviceFactory.createApplicationService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            TemporaryApplicationDTO.throwIfNotValid(request);
            final var temporaryApplicationDTO = getTemporaryApplicationDTOFromRequest(request);
            applicationService.makeTemporaryApplication(temporaryApplicationDTO);
            response.sendRedirect(request.getContextPath() + SUCCESS_APPLY_PAGE);
        } catch (final InvalidDataException e) {
            log.error("Validation error: " + e.getMessage());
            request.setAttribute("error", e.getInvalidField() + "_is_invalid");
            request.getRequestDispatcher(MAKE_TEMPORARY_APPLICATION_PAGE).forward(request, response);
        } catch (final ClientHasNotCanceledApplicationException e) {
            log.error(e.getMessage());
            response.sendRedirect(request.getContextPath() + CLIENT_HAS_APPLICATION_PAGE);
        } catch (final ApartmentsNotFoundException e) {
            log.error(e.getMessage());
            request.setAttribute("error", ERROR_PROPERTY_NAME);
            request.getRequestDispatcher(MAKE_TEMPORARY_APPLICATION_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }

    private TemporaryApplicationDTO getTemporaryApplicationDTOFromRequest(final HttpServletRequest request) {
        final var stayLength = request.getParameter("stay_length");
        final var apartmentClass = request.getParameter("apartment_class_id");
        final var numberOfPeople = request.getParameter("number_of_people");
        return TemporaryApplicationDTO.builder()
                .stayLength(parseInt(stayLength))
                .apartmentClass(ApartmentClass.getById(parseInt(apartmentClass)))
                .numberOfPeople(parseInt(numberOfPeople))
                .clientLogin(getLoginFromSession(request.getSession()))
                .build();
    }
}