package com.example.hotel.controller.action.client;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static java.lang.Long.parseLong;

public class CancelApplicationAction implements Action {
  public final static Logger log = Logger.getLogger(CancelApplicationAction.class);
  private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

  public CancelApplicationAction() {
  }

  public CancelApplicationAction(final ServiceFactory serviceFactory) {
    applicationService = serviceFactory.createApplicationService();
  }


  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      final var applicationId = parseLong(request.getParameter(RequestParameters.ID));
      applicationService.cancel(applicationId);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.APPLICATION_CANCELED);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }
}
