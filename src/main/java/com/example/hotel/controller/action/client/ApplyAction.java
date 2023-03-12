package com.example.hotel.controller.action.client;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.factory.mapper.MapperFactory;
import com.example.hotel.controller.factory.validator.ValidatorFactory;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.controller.validator.Validator;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ApartmentIsNotAvailableException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ApplyAction implements Action {
  public static final Logger log = Logger.getLogger(ApplyAction.class);
  private final ApplicationService applicationService;
  private final Mapper<ApplicationDTO> applicationMapper = MapperFactory.getInstance().createApplicationMapper();
  private final Validator applicationValidator = ValidatorFactory.getInstance().createApplicationValidator();

  public ApplyAction() {
    applicationService = ServiceFactory.getInstance().createApplicationService();
  }

  public ApplyAction(final ServiceFactory serviceFactory) {
    applicationService = serviceFactory.createApplicationService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      applicationValidator.throwIfNotValid(request);
      final var applicationDto = applicationMapper.map(request);
      applicationService.apply(applicationDto);

      request.getRequestDispatcher(Path.Get.Client.SUCCESS_APPLY_PAGE).forward(request, response);
    } catch (final InvalidDataException e) {
      log.error("Validation error: " + e.getMessage());
      response.sendRedirect(request.getContextPath() + Path.Get.Client.WRONG_STAY_LENGTH);
    } catch (final ClientHasNotCanceledApplicationException e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.CLIENT_HAS_APPLICATION_PAGE);
    } catch (final ApartmentIsNotAvailableException e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.APARTMENT_NOT_AVAILABLE_PAGE);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }
}
