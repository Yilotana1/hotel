package com.example.hotel.controller.command;

import com.example.hotel.commons.Constants.RequestAttributes;
import com.example.hotel.commons.Constants.RequestParameters;
import com.example.hotel.commons.Constants.SessionAttributes;
import com.example.hotel.commons.Path;
import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SignUpCommand implements Command {
    public static final Logger log = Logger.getLogger(SignUpCommand.class);
    public static final String ERROR_ATTRIBUTE = RequestAttributes.ERROR_PREFIX + Path.Get.User.SIGNUP_PAGE;
    private UserService userService = ServiceFactory.getInstance().createUserService();

    public SignUpCommand() {
    }

    public SignUpCommand(ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }


    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        log.trace("Started SignUp command");
        try {
            UserDTO.throwIfNotValid(request);
            final var userDTO = getUserDTO(request);
            log.trace("User validation passed successfully");

            userService.signUp(userDTO);
            log.trace("User has been signed up");

        } catch (final InvalidDataException e) {
            log.error(e.getMessage());
            setValidationErrorMessage(request, e);
            response.sendRedirect(request.getContextPath() + Path.Get.User.SIGNUP_PAGE);
            return;
        } catch (final LoginIsNotUniqueException e) {
            log.error(e.getMessage());
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "user_with_such_login_exists");
            response.sendRedirect(request.getContextPath() + Path.Get.User.SIGNUP_PAGE);
            return;
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "data_could_not_be_saved");
            response.sendRedirect(request.getContextPath() + Path.Get.User.SIGNUP_PAGE);
            return;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
        response.sendRedirect(
                request.getServletContext().getContextPath() + Path.Get.User.MAIN);
        log.debug("Registration passed successfully: redirect to profile");
    }

    private static void setValidationErrorMessage(final HttpServletRequest request,
                                                  final InvalidDataException e) {
        final var errorMessage = e.getInvalidField() + SessionAttributes.INVALID_SUFFIX;
        request
                .getSession()
                .setAttribute(ERROR_ATTRIBUTE, errorMessage);
    }


    private UserDTO getUserDTO(final HttpServletRequest request) {
        final var login = request.getParameter(RequestParameters.LOGIN);
        final var firstname = request.getParameter(RequestParameters.FIRSTNAME);
        final var lastname = request.getParameter(RequestParameters.LASTNAME);
        final var email = request.getParameter(RequestParameters.EMAIL);
        final var phone = request.getParameter(RequestParameters.PHONE);
        final var password = request.getParameter(RequestParameters.PASSWORD);
        return UserDTO.builder()
                .login(login)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .phone(phone)
                .password(password)
                .build();
    }
}
