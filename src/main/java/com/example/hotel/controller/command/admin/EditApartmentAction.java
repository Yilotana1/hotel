package com.example.hotel.controller.command.admin;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.command.Action;
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

import static com.example.hotel.controller.commons.Tools.setInvalidFieldMessage;
import static com.example.hotel.model.entity.enums.ApartmentStatus.BUSY;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class EditApartmentAction implements Action {

    private final static Logger log = Logger.getLogger(EditApartmentAction.class);
    public static final String ERROR_MESSAGE = "apartment_not_allowed_to_update";
    private final String errorAttribute = RequestAttributes.ERROR_PREFIX + Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT;
    private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();

    public EditApartmentAction() {
    }

    public EditApartmentAction(final ServiceFactory serviceFactory) {
        apartmentService = serviceFactory.createApartmentService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            UpdateApartmentDTO.throwIfNotValid(request);
            final var apartmentDTO = getApartmentDTOFromRequest(request);
            apartmentService.update(apartmentDTO);
            response.sendRedirect(request.getContextPath() + Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT);

        } catch (final InvalidDataException e) {
            log.error(e.getMessage(), e);
            setInvalidFieldMessage(request, e, errorAttribute);
            response.sendRedirect(request.getContextPath() + Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT);
        } catch (final ApartmentNotAllowedToUpdateException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(errorAttribute, ERROR_MESSAGE);
            response.sendRedirect(request.getContextPath() + Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private UpdateApartmentDTO getApartmentDTOFromRequest(final HttpServletRequest request) {
        final var status = request.getParameter(RequestParameters.BUSY_STATUS) != null ? BUSY : null;
        final var price = request.getParameter(RequestParameters.PRICE);
        return UpdateApartmentDTO.builder()
                .number(parseLong(request.getParameter(RequestParameters.APARTMENT_NUMBER)))
                .apartmentClass(
                        ApartmentClass
                                .getById(parseInt(request.getParameter(RequestParameters.APARTMENT_CLASS_ID))))
                .status(status)
                .price(price)
                .build();
    }
}
