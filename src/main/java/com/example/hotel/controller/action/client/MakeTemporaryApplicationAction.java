package com.example.hotel.controller.action.client;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.TemporaryApplicationDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.controller.factory.mapper.MapperFactory;
import com.example.hotel.controller.factory.validator.ValidatorFactory;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.controller.validator.Validator;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ApartmentsNotFoundException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.commons.Tools.setInvalidFieldMessage;

public class MakeTemporaryApplicationAction implements Action {
  public final static Logger log = Logger.getLogger(MakeTemporaryApplicationAction.class);
  public static final String ERROR_PROPERTY_NAME = "apartments_with_such_parameters_dont_exist";
  private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();
  private final Mapper<TemporaryApplicationDTO> temporaryApplicationMapper = MapperFactory
          .getInstance()
          .createTemporaryApplicationMapper();
  private final Validator temporaryApplicationValidator = ValidatorFactory
          .getInstance()
          .createTemporaryApplicationValidator();

  public static final String ERROR_ATTRIBUTE =
          RequestAttributes.ERROR_PREFIX + Path.Get.Client.MAKE_TEMPORARY_APPLICATION_PAGE;

  public MakeTemporaryApplicationAction() {
  }

  public MakeTemporaryApplicationAction(final ServiceFactory serviceFactory) {
    applicationService = serviceFactory.createApplicationService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      temporaryApplicationValidator.throwIfNotValid(request);
      final var temporaryApplicationDTO = temporaryApplicationMapper.map(request);
      applicationService.makeTemporaryApplication(temporaryApplicationDTO);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.SUCCESS_APPLY_PAGE);

    } catch (final InvalidDataException e) {
      log.error("Validation error: " + e.getMessage(), e);
      setInvalidFieldMessage(request, e, ERROR_ATTRIBUTE);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.MAKE_TEMPORARY_APPLICATION_PAGE);
    } catch (final ClientHasNotCanceledApplicationException e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.CLIENT_HAS_APPLICATION_PAGE);
    } catch (final ApartmentsNotFoundException e) {
      log.error(e.getMessage(), e);
      request.getSession().setAttribute(ERROR_ATTRIBUTE, ERROR_PROPERTY_NAME);
      response.sendRedirect(request.getContextPath() + Path.Post.Client.MAKE_TEMPORARY_APPLICATION);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }
}