package com.example.hotel.controller.command.client;

import com.example.hotel.controller.command.Action;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static com.example.hotel.controller.commons.Tools.getLocalDateFromString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfirmPaymentActionTest {
  private static final String ID = "1";
  private static final String START_DATE = "2022-11-10";
  private static final String END_DATE = "2022-11-15";

  private final ApplicationService applicationService = mock(ApplicationService.class);
  private Action confirmPaymentAction;

  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);

  @BeforeEach
  void init() {
    final var serviceFactory = mock(ServiceFactory.class);
    when(serviceFactory.createApplicationService()).thenReturn(applicationService);
    when(request.getSession()).thenReturn(mock(HttpSession.class));
    when(request.getContextPath()).thenReturn(BASE_URL);
    when(request.getParameter(RequestParameters.ID)).thenReturn(ID);
    when(request.getParameter(RequestParameters.START_DATE)).thenReturn(START_DATE);
    when(request.getParameter(RequestParameters.END_DATE)).thenReturn(END_DATE);
    final var servletContext = mock(ServletContext.class);
    when(request.getServletContext()).thenReturn(servletContext);
    confirmPaymentAction = new ConfirmPaymentAction(serviceFactory);
  }

  @Test
  void givenApplicationId_execute_shouldConfirmPayment() throws ServletException, IOException, ParseException {
    confirmPaymentAction.execute(request, response);

    verify(applicationService, times(1))
            .confirmPayment(
                    Long.parseLong(ID),
                    getLocalDateFromString(START_DATE),
                    getLocalDateFromString(END_DATE)
            );
    verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.PROFILE);
  }

  @Test
  void givenUndefinedException_execute_shouldSendRedirectToErrorPage()
          throws ServletException, IOException, ParseException {
    doThrow(RuntimeException.class).when(applicationService)
            .confirmPayment(
                    Long.parseLong(ID),
                    getLocalDateFromString(START_DATE),
                    getLocalDateFromString(END_DATE)
            );
    confirmPaymentAction.execute(request, response);

    verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.Error.ERROR_503);
  }
}