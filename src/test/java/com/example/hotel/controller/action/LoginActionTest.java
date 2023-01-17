package com.example.hotel.controller.action;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Constants.ServletContextAttributes;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.model.entity.User;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.example.hotel.controller.action.LoginAction.EMPTY_CREDENTIALS_MESSAGE_PROPERTY;
import static com.example.hotel.controller.action.LoginAction.ERROR_ATTRIBUTE;
import static com.example.hotel.controller.action.LoginAction.USER_IN_SYSTEM_MESSAGE_PROPERTY;
import static com.example.hotel.controller.action.LoginAction.USER_NOT_FOUND_MESSAGE_PROPERTY;
import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static com.example.hotel.model.entity.enums.Role.CLIENT;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginActionTest {

    private final UserService userService = mock(UserService.class);

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    private Action loginAction;
    private final Set<String> loginCache = new HashSet<>();

    private final User user = User.builder()
            .login("login")
            .password("password")
            .roles(Set.of(CLIENT))
            .status(NON_BLOCKED)
            .build();

    private void mockRequestParameters() {
        when(request.getParameter(RequestParameters.LOGIN)).thenReturn(user.getLogin());
        when(request.getParameter(RequestParameters.PASSWORD)).thenReturn(user.getPassword());
    }

    @BeforeEach
    void init() {
        final var serviceFactory = mock(ServiceFactory.class);
        when(serviceFactory.createUserService()).thenReturn(userService);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getContextPath()).thenReturn(BASE_URL);
        final var servletContext = mock(ServletContext.class);
        when(servletContext.getAttribute(ServletContextAttributes.LOGGED_USERS))
                .thenReturn(loginCache);
        when(request.getServletContext()).thenReturn(servletContext);

        loginAction = new LoginAction(serviceFactory);
    }

    @Test
    void givenLoginAndPassword_execute_shouldAddDataToContextAndRedirectToProfile()
            throws ServletException, IOException {
        mockRequestParameters();
        when(userService.signIn(user.getLogin(), user.getPassword())).thenReturn(Optional.of(user));

        loginAction.execute(request, response);
        assertThat(loginCache).contains(user.getLogin());
        verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.PROFILE);
        verify(request.getSession(), times(1))
                .setAttribute(SessionAttributes.ROLES, user.getRoles());
        verify(request.getSession(), times(1))
                .setAttribute(SessionAttributes.LOGIN, user.getLogin());
        verify(request.getSession(), times(1))
                .setAttribute(SessionAttributes.STATUS, user.getStatus());
    }

    @Test
    void givenEmptyCredentials_execute_shouldPutErrorMessageToSessionAndRedirectToLoginPage()
            throws IOException, ServletException {

        loginAction.execute(request, response);
        verify(userService, never()).signIn(any(String.class), any(String.class));
        verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.LOGIN_PAGE);
        verify(request.getSession(), times(1))
                .setAttribute(ERROR_ATTRIBUTE, EMPTY_CREDENTIALS_MESSAGE_PROPERTY);
    }

    @Test
    void givenCredentialsOfAlreadyLoggedUser_execute_shouldPutErrorMessageToSessionAndRedirectToLoginPage()
            throws IOException, ServletException {
        mockRequestParameters();
        loginCache.add(user.getLogin());

        loginAction.execute(request, response);
        verify(userService, never()).signIn(any(String.class), any(String.class));
        verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.LOGIN_PAGE);
        verify(request.getSession(), times(1))
                .setAttribute(ERROR_ATTRIBUTE, USER_IN_SYSTEM_MESSAGE_PROPERTY);
    }

    @Test
    void givenWrongCredentials_execute_shouldPutErrorMessageToSessionAndRedirectToLoginPage()
            throws IOException, ServletException {
        mockRequestParameters();
        when(userService.signIn(user.getLogin(), user.getPassword())).thenReturn(Optional.empty());

        loginAction.execute(request, response);
        verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.LOGIN_PAGE);
        verify(request.getSession(), times(1))
                .setAttribute(ERROR_ATTRIBUTE, USER_NOT_FOUND_MESSAGE_PROPERTY);
    }

    @Test
    void givenUndefinedError_execute_shouldHandeException()
            throws IOException, ServletException {
        mockRequestParameters();
        when(userService.signIn(user.getLogin(), user.getPassword())).thenThrow(RuntimeException.class);

        loginAction.execute(request, response);
        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.Error.ERROR_503);
    }
}