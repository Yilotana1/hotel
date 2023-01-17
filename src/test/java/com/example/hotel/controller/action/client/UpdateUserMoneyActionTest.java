package com.example.hotel.controller.action.client;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateUserMoneyActionTest {

  public static final String INCORRECT_MONEY_INPUT = "dasaspmo";
  private final UserService userService = mock(UserService.class);
  private Action updateUserMoneyAction;

  private static final String MONEY = "123";
  private static final String LOGIN = "login";

  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);

  @BeforeEach
  void init() {
    final var serviceFactory = mock(ServiceFactory.class);
    when(serviceFactory.createUserService()).thenReturn(userService);
    final var session = mock(HttpSession.class);
    when(session.getAttribute(SessionAttributes.LOGIN)).thenReturn(LOGIN);
    when(request.getSession()).thenReturn(session);
    when(request.getContextPath()).thenReturn(BASE_URL);
    when(request.getParameter(RequestParameters.CLIENT_MONEY)).thenReturn(MONEY);
    final var servletContext = mock(ServletContext.class);
    when(request.getServletContext()).thenReturn(servletContext);
    updateUserMoneyAction = new UpdateUserMoneyAction(serviceFactory);
  }

  @Test
  void givenMoneyInputAndLoginFromSession_execute_shouldUpdateUserMoney()
          throws ServletException, IOException {

    updateUserMoneyAction.execute(request, response);

    verify(userService, times(1)).updateMoneyAccount(LOGIN, new BigDecimal(MONEY));
    verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.PROFILE);
  }

  @Test
  void givenIncorrectMoneyInput_execute_shouldSendRedirectToErrorPage()
          throws ServletException, IOException {
    when(request.getParameter(RequestParameters.CLIENT_MONEY)).thenReturn(INCORRECT_MONEY_INPUT);

    updateUserMoneyAction.execute(request, response);

    verify(userService, never()).updateMoneyAccount(anyString(), any(BigDecimal.class));
    verify(response, times(1))
            .sendRedirect(BASE_URL + Path.Get.Client.MONEY_VALUE_IS_INCORRECT);
  }


}