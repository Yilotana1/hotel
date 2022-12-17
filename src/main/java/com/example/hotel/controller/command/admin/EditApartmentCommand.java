package com.example.hotel.controller.command.admin;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.entity.enums.ApartmentClass;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ApartmentNotAllowedToUpdateException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.SHOW_APARTMENTS_MANAGEMENT;
import static com.example.hotel.model.entity.enums.ApartmentStatus.BUSY;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class EditApartmentCommand implements Command {

    private final static Logger log = Logger.getLogger(EditApartmentCommand.class);
    private static final String NUMBER_REQUEST_PARAMETER = "number";
    private static final String CLASS_REQUEST_PARAMETER = "class_id";
    private static final String STATUS_REQUEST_PARAMETER = "busy";
    private static final String PRICE_REQUEST_PARAMETER = "price";
    private static final String ERROR_ATTRIBUTE = "error";
    public static final String APARTMENT_NOT_ALLOWED_TO_UPDATE = "apartment_not_allowed_to_update";
    public static final String WHITESPACE = " ";
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();

    public EditApartmentCommand() {
    }

    public EditApartmentCommand(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            UpdateApartmentDTO.throwIfNotValid(request);
            final var apartmentDTO = getApartmentDTOFromRequest(request);
            apartmentService.update(apartmentDTO);
            response.sendRedirect(request.getContextPath() + SHOW_APARTMENTS_MANAGEMENT);
        } catch (final InvalidDataException e) {
            log.error(e.getMessage(), e);
            request.setAttribute(ERROR_ATTRIBUTE, e.getInvalidField() + "_is_invalid");
            request.getRequestDispatcher(SHOW_APARTMENTS_MANAGEMENT).forward(request, response);
        } catch (final ApartmentNotAllowedToUpdateException e) {
            log.error(e.getMessage(), e);
            request.setAttribute(ERROR_ATTRIBUTE, APARTMENT_NOT_ALLOWED_TO_UPDATE);
            request.getRequestDispatcher(SHOW_APARTMENTS_MANAGEMENT).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
    }

    private UpdateApartmentDTO getApartmentDTOFromRequest(final HttpServletRequest request) {
        final var status = request.getParameter(STATUS_REQUEST_PARAMETER) != null ? BUSY : null;
        final var price = request.getParameter(PRICE_REQUEST_PARAMETER);
        return UpdateApartmentDTO.builder()
                .number(parseLong(request.getParameter(NUMBER_REQUEST_PARAMETER)))
                .apartmentClass(ApartmentClass.getById(parseInt(request.getParameter(CLASS_REQUEST_PARAMETER))))
                .status(status)
                .price(new BigDecimal(price.substring(0, price.indexOf(WHITESPACE))))
                .build();
    }
}
