package com.example.hotel.controller.command;

import com.example.hotel.controller.Path;
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

import static com.example.hotel.controller.Path.SIGNUP_PAGE;

public class SignUpCommand implements Command {
    public final static Logger log = Logger.getLogger(SignUpCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.trace("Started SignUp command");
        var userDTO = getUserDTOFromRequest(request);
        try {
            userDTO.throwIfNotValid();
            log.trace("User validation passed successfully");

            var userService = ServiceFactory.getInstance().createUserService();
            userService.signUp(userDTO);
            log.trace("User has been signed up");

        } catch (InvalidDataException e) {
            log.error("Validation error: " + e.getMessage());
            request.setAttribute("error", e.getInvalidField() + " is invalid");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (LoginIsNotUniqueException e) {
            log.error("LoginIsNotUnique exception: " + e.getMessage());
            request.setAttribute("error", "User with such login already exists");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        } catch (ServiceException e) {
            log.error("Service exception: " + e.getMessage());
            request.setAttribute("error", "Something went wrong. Your data could not be saved.");
            request.getRequestDispatcher(SIGNUP_PAGE).forward(request, response);
            return;
        }

        response.sendRedirect(
                request.getServletContext().getContextPath() + Path.MAIN_PAGE);
        log.debug("Registration passed successfully: redirect to profile");
    }


    private UserDTO getUserDTOFromRequest(HttpServletRequest request) {
        return UserDTO.builder()
                .login(request.getParameter("login"))
                .firstname(request.getParameter("firstname"))
                .lastname(request.getParameter("lastname"))
                .email(request.getParameter("email"))
                .phone(request.getParameter("phone"))
                .password(request.getParameter("password"))
                .build();
    }
}
