package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Action;
import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.exception.ApplicationNotFoundException;
import com.example.hotel.model.entity.Application;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.commons.Tools.getLoginFromSession;
import static java.lang.String.format;

public class ShowApplicationInvoiceAction implements Action {
  public final static Logger log = Logger.getLogger(ShowApplicationInvoiceAction.class);
  private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();

  public ShowApplicationInvoiceAction() {
  }

  public ShowApplicationInvoiceAction(final ServiceFactory serviceFactory) {
    applicationService = serviceFactory.createApplicationService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    try {
      final var login = getLoginFromSession(request.getSession());
      final var application = getApplicationFromService(login);

      request.setAttribute(RequestAttributes.APPLICATION, application);
      request.getRequestDispatcher(Path.Get.Client.APPLICATION_INVOICE_PAGE).forward(request, response);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }

  private Application getApplicationFromService(final String login) {
    return applicationService
            .getApplicationToConfirm(login)
            .orElseThrow(() -> new ApplicationNotFoundException(
                    format("Application of client with login - %s not found", login)));
  }
}
