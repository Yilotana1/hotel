package com.example.hotel.controller.action.admin;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.UpdateApartmentDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.factory.mapper.MapperFactory;
import com.example.hotel.controller.factory.validator.ValidatorFactory;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.controller.validator.Validator;
import com.example.hotel.model.service.ApartmentService;
import com.example.hotel.model.service.exception.ApartmentNotAllowedToUpdateException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.commons.Tools.setInvalidFieldMessage;

public class EditApartmentAction implements Action {

  private final static Logger log = Logger.getLogger(EditApartmentAction.class);
  public static final String ERROR_MESSAGE = "apartment_not_allowed_to_update";
  private final String errorAttribute = RequestAttributes.ERROR_PREFIX + Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT;
  private ApartmentService apartmentService = ServiceFactory.getInstance().createApartmentService();
  private final Validator apartmentValidator = ValidatorFactory.getInstance().createApartmentValidator();
  private final Mapper<UpdateApartmentDTO> apartmentMapper = MapperFactory.getInstance().createApartmentMapper();

  public EditApartmentAction() {
  }

  public EditApartmentAction(final ServiceFactory serviceFactory) {
    apartmentService = serviceFactory.createApartmentService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      apartmentValidator.throwIfNotValid(request);
      final var apartmentDto = apartmentMapper.map(request);
      apartmentService.update(apartmentDto);
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
}
