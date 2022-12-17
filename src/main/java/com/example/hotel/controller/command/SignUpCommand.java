package com.example.hotel.controller.command;

import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.controller.exception.InvalidDataException;
import com.example.hotel.model.service.exception.LoginIsNotUniqueException;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.MAIN;
import static com.example.hotel.controller.Path.SIGNUP_PAGE;

public class SignUpCommand implements Command {
    public static final Logger log = Logger.getLogger(SignUpCommand.class);
    public static final String USER_WITH_SUCH_LOGIN_EXISTS = "user_with_such_login_exists";
    public static final String DATA_COULD_NOT_BE_SAVED = "data_could_not_be_saved";
    public static final String ERROR_ATTRIBUTE = "error";

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        log.trace("Started SignUp command");
        try {
            UserDTO.throwIfNotValid(request);
            final var userDTO = getUserDTO(request);
            log.trace("User validation passed successfully");

            final var userService = ServiceFactory.getInstance().createUserService();
            userService.signUp(userDTO);
            log.trace("User has been signed up");

        } catch (final InvalidDataException e) {
            log.error(e.getMessage());
            request.setAttribute(ERROR_ATTRIBUTE, e.getInvalidField() + "_is_invalid");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (final LoginIsNotUniqueException e) {
            log.error(e.getMessage());
            request.setAttribute(ERROR_ATTRIBUTE, USER_WITH_SUCH_LOGIN_EXISTS);
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            request.setAttribute(ERROR_ATTRIBUTE, DATA_COULD_NOT_BE_SAVED);
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }

        response.sendRedirect(
                request.getServletContext().getContextPath() + MAIN);
        log.debug("Registration passed successfully: redirect to profile");
    }


    private UserDTO getUserDTO(final HttpServletRequest request) {
        final var login = request.getParameter("login");
        final var firstname = request.getParameter("firstname");
        final var lastname = request.getParameter("lastname");
        final var email = request.getParameter("email");
        final var phone = request.getParameter("phone");
        final var password = request.getParameter("password");
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
