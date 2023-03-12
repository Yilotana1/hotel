package com.example.hotel.controller.action.manager;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.controller.factory.mapper.MapperFactory;
import com.example.hotel.controller.mapper.Mapper;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

public class ApplyForClientAction implements Action {
  public static final Logger log = Logger.getLogger(ApplyForClientAction.class);
  private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();
  private final Mapper<ApplicationDTO> applicationMapper = MapperFactory.getInstance().createApplicationMapper();

  public ApplyForClientAction() {
  }

  public ApplyForClientAction(final ServiceFactory serviceFactory) {
    applicationService = serviceFactory.createApplicationService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      final var applicationDTO = applicationMapper.map(request);
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
}
