package com.example.hotel.controller.action.admin;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.action.Action;
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

import static com.example.hotel.model.entity.enums.UserStatus.BLOCKED;
import static com.example.hotel.model.entity.enums.UserStatus.NON_BLOCKED;

public class EditUserAction implements Action {

    public final static Logger log = Logger.getLogger(EditUserAction.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();
    public EditUserAction() {
    }

    public EditUserAction(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var id = Integer.parseInt(request.getParameter(RequestParameters.ID));
            final var status = request
                    .getParameter(RequestParameters.BLOCKED_STATUS) != null ? BLOCKED : NON_BLOCKED;
            final var roles = getRoles(request);
            userService.update(id, status, roles);
            response.sendRedirect(request.getContextPath() + Path.Get.Admin.ADMIN_MANAGE_USERS);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }

    private Collection<Role> getRoles(final HttpServletRequest request) {
        final var roles = new HashSet<Role>();
        for (final var role : Role.values()) {
            if (request.getParameter(role.name()) != null) {
                roles.add(role);
            }
        }
        return roles;
    }

}
