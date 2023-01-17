package com.example.hotel.controller.action;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.example.hotel.controller.action.SignUpAction.ERROR_ATTRIBUTE;
import static com.example.hotel.controller.action.SignUpAction.GENERAL_ERROR_MESSAGE_PROPERTY;
import static com.example.hotel.controller.action.SignUpAction.USER_EXISTS_MESSAGE_PROPERTY;
import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class SignUpActionTest {

    private static final String NULL_STRING = "null";
    private final UserService userService = mock(UserService.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);


    private Action signUpAction;


    private void mockRequestParameters() {
        when(request.getParameter(RequestParameters.LOGIN)).thenReturn("login");
        when(request.getParameter(RequestParameters.FIRSTNAME)).thenReturn("firstname");
        when(request.getParameter(RequestParameters.LASTNAME)).thenReturn("lastname");
        when(request.getParameter(RequestParameters.EMAIL)).thenReturn("email@gmail.com");
        when(request.getParameter(RequestParameters.PASSWORD)).thenReturn("password");
        when(request.getParameter(RequestParameters.PHONE)).thenReturn("+38031231412");
    }

    @BeforeEach
    void init() {
        final var serviceFactory = mock(ServiceFactory.class);
        when(serviceFactory.createUserService()).thenReturn(userService);
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getContextPath()).thenReturn(BASE_URL);

        signUpAction = new SignUpAction(serviceFactory);
    }

    @Test
    void givenBadRequestData_execute_shouldHandleInvalidDataException() throws ServletException, IOException {
        final var errorMessage = NULL_STRING + SessionAttributes.INVALID_SUFFIX;

        signUpAction.execute(request, response);

        verify(userService, never()).signUp(any(UserDTO.class));
        verify(request.getSession(), times(1))
                .setAttribute(ERROR_ATTRIBUTE, errorMessage);
        verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.SIGNUP_PAGE);
    }

    @Test
    void givenRequest_execute_shouldSignUpNewUser() throws ServletException, IOException {
        mockRequestParameters();

        signUpAction.execute(request, response);

        verify(userService, times(1)).signUp(any(UserDTO.class));
        verify(response, times(1)).sendRedirect(BASE_URL + Path.Get.User.MAIN);
    }

    @Test
    void givenRequestWithNotUniqueLogin_execute_shouldHandleLoginIsNotUniqueException()
            throws ServletException, IOException {
        mockRequestParameters();
        when(userService.signUp(any(UserDTO.class))).thenThrow(LoginIsNotUniqueException.class);

        signUpAction.execute(request, response);
        verify(request.getSession(), times(1))
                .setAttribute(ERROR_ATTRIBUTE, USER_EXISTS_MESSAGE_PROPERTY);
        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.User.SIGNUP_PAGE);
    }

    @Test
    void givenErrorOnServiceLayer_execute_shouldHandleServiceException()
            throws ServletException, IOException {
        mockRequestParameters();
        when(userService.signUp(any(UserDTO.class))).thenThrow(ServiceException.class);

        signUpAction.execute(request, response);
        verify(request.getSession(), times(1))
                .setAttribute(ERROR_ATTRIBUTE, GENERAL_ERROR_MESSAGE_PROPERTY);
        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.User.SIGNUP_PAGE);
    }

    @Test
    void givenUndefinedError_execute_shouldHandleException()
            throws ServletException, IOException {
        mockRequestParameters();
        when(userService.signUp(any(UserDTO.class))).thenThrow(RuntimeException.class);

        signUpAction.execute(request, response);
        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.Error.ERROR_503);
    }

}