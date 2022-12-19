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

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.PROFILE;
import static com.example.hotel.model.dao.Tools.addLoginToCache;

public class EditProfileCommand implements Command {

    public final static Logger log = Logger.getLogger(EditProfileCommand.class);
    public static final String ERROR_ATTRIBUTE = "error" + PROFILE;
    public static final String LOGIN_ATTRIBUTE = "login";
    private UserService userService = ServiceFactory.getInstance().createUserService();

    public EditProfileCommand() {
    }

    public EditProfileCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDTO.throwIfNotValid(request);
            final var userDTO = getUserDTO(request);
            userService.update(userDTO);
            addLoginToCache(userDTO.getLogin(), request);
            request.getSession().setAttribute(LOGIN_ATTRIBUTE, userDTO.getLogin());

        } catch (final InvalidDataException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, e.getInvalidField() + "_is_invalid");
        } catch (final LoginIsNotUniqueException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "user_with_such_login_exists");
        } catch (final ServiceException e) {
            log.error(e.getMessage(), e);
            request.getSession().setAttribute(ERROR_ATTRIBUTE, "data_could_not_be_saved");
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        } finally {
            response.sendRedirect(request.getContextPath() + PROFILE);
        }
    }

    private UserDTO getUserDTO(final HttpServletRequest request) {
        return UserDTO
                .builder()
                .login(request.getParameter("login"))
                .firstname(request.getParameter("firstname"))
                .lastname(request.getParameter("lastname"))
                .phone(request.getParameter("phone"))
                .email(request.getParameter("email"))
                .password(request.getParameter("password"))
                .build();
    }
}
