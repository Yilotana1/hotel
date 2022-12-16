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
    public final static Logger log = Logger.getLogger(SignUpCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.trace("Started SignUp command");
        try {
            var userDTO = getUserDTO(request);
            userDTO.throwIfNotValid();
            log.trace("User validation passed successfully");

            var userService = ServiceFactory.getInstance().createUserService();
            userService.signUp(userDTO);
            log.trace("User has been signed up");

        } catch (InvalidDataException e) {
            log.error("Validation error: " + e.getMessage());
            request.setAttribute("error", e.getInvalidField() + "_is_invalid");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (LoginIsNotUniqueException e) {
            log.error("LoginIsNotUnique exception: " + e.getMessage());
            request.setAttribute("error", "user_with_such_login_exists");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (ServiceException e) {
            log.error("Service exception: " + e.getMessage());
            request.setAttribute("error", "data_could_not_be_saved");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }

        response.sendRedirect(
                request.getServletContext().getContextPath() + MAIN);
        log.debug("Registration passed successfully: redirect to profile");
    }


    private UserDTO getUserDTO(HttpServletRequest request) {
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
