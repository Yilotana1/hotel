package com.example.hotel.controller.command;

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

import static com.example.hotel.controller.Path.Get.User.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.Get.User.MAIN;
import static com.example.hotel.controller.Path.Get.User.SIGNUP_PAGE;

public class SignUpCommand implements Command {
    public static final Logger log = Logger.getLogger(SignUpCommand.class);
    public static final String USER_WITH_SUCH_LOGIN_EXISTS = "user_with_such_login_exists";
    public static final String DATA_COULD_NOT_BE_SAVED = "data_could_not_be_saved";
    public static final String ERROR_ATTRIBUTE = "error" + SIGNUP_PAGE;

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
            request.getSession().setAttribute(ERROR_ATTRIBUTE, e.getInvalidField() + "_is_invalid");
            response.sendRedirect(request.getContextPath() + SIGNUP_PAGE);
            return;
        } catch (final LoginIsNotUniqueException e) {
            log.error(e.getMessage());
            request.getSession().setAttribute(ERROR_ATTRIBUTE, USER_WITH_SUCH_LOGIN_EXISTS);
            response.sendRedirect(request.getContextPath() + SIGNUP_PAGE);
            return;
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, DATA_COULD_NOT_BE_SAVED);
            response.sendRedirect(request.getContextPath() + SIGNUP_PAGE);
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
