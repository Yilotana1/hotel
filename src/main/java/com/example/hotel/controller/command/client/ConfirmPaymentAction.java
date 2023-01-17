package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Action;
import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.NotEnoughMoneyToConfirmException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.commons.Tools.getLocalDateFromString;

public class ConfirmPaymentAction implements Action {
  public final static Logger log = Logger.getLogger(ConfirmPaymentAction.class);
  private static final String ERROR_MESSAGE = "client_has_not_enough_to_confirm";
  private ApplicationService applicationService = ServiceFactory.getInstance().createApplicationService();
  public final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.Client.APPLICATION_INVOICE;

  public ConfirmPaymentAction() {
  }

  public ConfirmPaymentAction(final ServiceFactory serviceFactory) {
    applicationService = serviceFactory.createApplicationService();
  }

  @Override
  public void execute(final HttpServletRequest request, final HttpServletResponse response)
          throws ServletException, IOException {
    try {
      final var applicationId = Long.parseLong(
              request.getParameter(RequestParameters.ID)
      );
      final var startDate = getLocalDateFromString(
              request.getParameter(RequestParameters.START_DATE)
      );
      final var endDate = getLocalDateFromString(
              request.getParameter(RequestParameters.END_DATE)
      );

      applicationService.confirmPayment(applicationId, startDate, endDate);
      log.trace("Payment is confirmed");
      response.sendRedirect(request.getContextPath() + Path.Get.User.PROFILE);
    } catch (final NotEnoughMoneyToConfirmException e) {
      log.error(e.getMessage(), e);
      request.getSession().setAttribute(ERROR_ATTRIBUTE, ERROR_MESSAGE);
      response.sendRedirect(request.getContextPath() + Path.Get.Client.APPLICATION_INVOICE);
    } catch (final Exception e) {
      log.error(e.getMessage(), e);
      response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
    }
  }
}
