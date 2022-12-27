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

import static com.example.hotel.controller.Path.Get.Client.CLIENT_HAS_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.Get.Client.MAKE_TEMPORARY_APPLICATION_PAGE;
import static com.example.hotel.controller.Path.Get.Client.SUCCESS_APPLY_PAGE;
import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static com.example.hotel.model.dao.Tools.getLoginFromSession;
import static java.lang.Integer.parseInt;

public class MakeTemporaryApplicationCommand implements Command {
    public final static Logger log = Logger.getLogger(MakeTemporaryApplicationCommand.class);
    public static final String ERROR_PROPERTY_NAME = "apartments_with_such_parameters_dont_exist";
    private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();
    public static final String ERROR_ATTRIBUTE = "error" + MAKE_TEMPORARY_APPLICATION_PAGE;

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
            log.error("Validation error: " + e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, e.getInvalidField() + "_is_invalid");
            response.sendRedirect(request.getContextPath() + MAKE_TEMPORARY_APPLICATION_PAGE);
        } catch (final ClientHasNotCanceledApplicationException e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + CLIENT_HAS_APPLICATION_PAGE);
        } catch (final ApartmentsNotFoundException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, ERROR_PROPERTY_NAME);
            response.sendRedirect(request.getContextPath() + MAKE_TEMPORARY_APPLICATION_PAGE);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
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