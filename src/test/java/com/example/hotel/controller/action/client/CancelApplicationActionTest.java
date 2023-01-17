package com.example.hotel.controller.action.client;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Constants;
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

import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CancelApplicationActionTest {
  private static final String ID = "1";
  private final ApplicationService applicationService = mock(ApplicationService.class);
  private Action cancelApplicationAction;

  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);

  @BeforeEach
  void init() {
    final var serviceFactory = mock(ServiceFactory.class);
    when(serviceFactory.createApplicationService()).thenReturn(applicationService);
    when(request.getSession()).thenReturn(mock(HttpSession.class));
    when(request.getContextPath()).thenReturn(BASE_URL);
    when(request.getParameter(Constants.RequestParameters.ID)).thenReturn(ID);
    final var servletContext = mock(ServletContext.class);
    when(request.getServletContext()).thenReturn(servletContext);
    cancelApplicationAction = new CancelApplicationAction(serviceFactory);
  }

  @Test
  void givenApplicationId_execute_shouldCancelApplication() throws ServletException, IOException {
    cancelApplicationAction.execute(request, response);

    verify(applicationService, times(1)).cancel(Long.parseLong(ID));
    verify(response, times(1))
            .sendRedirect(BASE_URL + Path.Get.Client.APPLICATION_CANCELED);
  }

  @Test
  void givenUndefinedException_execute_shouldSendRedirectToErrorPage() throws ServletException, IOException {
    doThrow(RuntimeException.class).when(applicationService).cancel(Long.parseLong(ID));

    cancelApplicationAction.execute(request, response);
    verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.Error.ERROR_503);
  }
}