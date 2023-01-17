package com.example.hotel.controller.action.client;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Constants.SessionAttributes;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.dto.ApplicationDTO;
import com.example.hotel.model.service.ApplicationService;
import com.example.hotel.model.service.exception.ApartmentIsNotAvailableException;
import com.example.hotel.model.service.exception.ClientHasNotCanceledApplicationException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplyActionTest {

    public static final HttpSession SESSION = mock(HttpSession.class);
    public static final ServletContext SERVLET_CONTEXT = mock(ServletContext.class);
    public static final String WRONG_STAY_LENGTH = "aaa";
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final ApplicationService applicationService = mock(ApplicationService.class);
    private Action applyAction;

    private final ApplicationDTO applicationDTO = ApplicationDTO.builder()
            .apartmentNumber(3)
            .stayLength(3)
            .clientLogin("login")
            .build();

    private void mockRequestParameters() {
        when(request.getParameter(RequestParameters.APARTMENT_NUMBER))
                .thenReturn(String.valueOf(applicationDTO.getApartmentNumber()));
        when(request.getParameter(RequestParameters.STAY_LENGTH))
                .thenReturn(String.valueOf(applicationDTO.getStayLength()));
    }

    @BeforeEach
    void init() {
        final var serviceFactory = mock(ServiceFactory.class);
        when(serviceFactory.createApplicationService()).thenReturn(applicationService);
        when(SESSION.getAttribute(SessionAttributes.LOGIN)).thenReturn(applicationDTO.getClientLogin());
        when(request.getSession()).thenReturn(SESSION);
        when(request.getContextPath()).thenReturn(BASE_URL);
        when(request.getServletContext()).thenReturn(SERVLET_CONTEXT);
        when(request.getRequestDispatcher(Path.Get.Client.SUCCESS_APPLY_PAGE))
                .thenReturn(mock(RequestDispatcher.class));
        applyAction = new ApplyAction(serviceFactory);
    }

    @Test
    void givenRequestParameters_execute_shouldMakeApplication() throws ServletException, IOException {
        mockRequestParameters();

        applyAction.execute(request, response);

        verify(applicationService, times(1)).apply(any(ApplicationDTO.class));
        verify(request.getRequestDispatcher(Path.Get.Client.SUCCESS_APPLY_PAGE)).forward(request, response);
    }

    @Test
    void givenWrongStayLength_execute_shouldHandleInvalidDataException() throws ServletException, IOException {
        mockRequestParameters();
        when(request.getParameter(RequestParameters.STAY_LENGTH)).thenReturn(WRONG_STAY_LENGTH);

        applyAction.execute(request, response);

        verify(applicationService, never()).apply(any(ApplicationDTO.class));
        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.Client.WRONG_STAY_LENGTH);
    }

    @Test
    void givenRequest_execute_shouldHandleClientHasNotCanceledApplicationException()
            throws ServletException, IOException {
        mockRequestParameters();
        doThrow(ClientHasNotCanceledApplicationException.class).when(applicationService)
                .apply(any(ApplicationDTO.class));

        applyAction.execute(request, response);

        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.Client.CLIENT_HAS_APPLICATION_PAGE);
    }

    @Test
    void givenRequest_execute_shouldHandleApartmentNotAvailableException()
            throws ServletException, IOException {
        mockRequestParameters();
        doThrow(ApartmentIsNotAvailableException.class).when(applicationService)
                .apply(any(ApplicationDTO.class));

        applyAction.execute(request, response);

        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.Client.APARTMENT_NOT_AVAILABLE_PAGE);
    }

    @Test
    void givenRequest_execute_shouldHandleGeneralException()
            throws ServletException, IOException {
        mockRequestParameters();
        doThrow(RuntimeException.class).when(applicationService)
                .apply(any(ApplicationDTO.class));

        applyAction.execute(request, response);

        verify(response, times(1))
                .sendRedirect(BASE_URL + Path.Get.Error.ERROR_503);
    }
}