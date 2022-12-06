package com.example.hotel.controller.command.admin;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.entity.enums.Role;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import static com.example.hotel.controller.Path.ADMIN_MANAGE_USERS;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.model.entity.enums.UserStatus.BLOCKED;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;

public class EditUserCommand implements Command {

    public final static Logger log = Logger.getLogger(EditUserCommand.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();

    public EditUserCommand() {
    }

    public EditUserCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var id = Integer.parseInt(request.getParameter("id"));
            final var status = request.getParameter("blocked") != null ? BLOCKED : NON_BLOCKED;
            final var roles = getRoles(request);
            userService.update(id, status, roles);
            request.getRequestDispatcher(ADMIN_MANAGE_USERS).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage());
            request.getRequestDispatcher(ERROR_503_PAGE).forward(request, response);
        }
    }

    private Collection<Role> getRoles(HttpServletRequest request) {
        final var roles = new HashSet<Role>();
        for (final var role : Role.values()) {
            if (request.getParameter(role.name()) != null) {
                roles.add(role);
            }
        }
        return roles;
    }

}
