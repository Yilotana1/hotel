package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.command.Action;
import com.example.hotel.controller.command.admin.ShowUsersManagementAction;
import com.example.hotel.model.entity.User;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.example.hotel.controller.commons.Tools.getPageId;

public class ShowUsersAction implements Action {
    public final static Logger log = Logger.getLogger(ShowUsersManagementAction.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();
    private static final int PAGE_SIZE = 10;
    public ShowUsersAction() {
    }

    public ShowUsersAction(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageNumber = getPageId(request);
            final var skip = (pageNumber - 1) * PAGE_SIZE;

            if (request.getParameter(RequestParameters.BY_LOGIN) == null) {
                final var users = userService.getUsers(skip, PAGE_SIZE);
                setUsersAttribute(request, users);
                final var totalPagesNumber = userService.count() / PAGE_SIZE;
                request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalPagesNumber);
                forwardToListUsersPage(request, response);
                return;
            }

            final var login = request.getParameter(RequestParameters.LOGIN);
            final var user = userService.getByLogin(login);
            user.ifPresent(u -> setUsersAttribute(request, List.of(u)));
            request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, user.map(u -> 1).orElse(0));
            forwardToListUsersPage(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }

    }

    private static void forwardToListUsersPage(final HttpServletRequest request,
                                               final HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(Path.Get.Manager.MANAGER_LIST_USERS_PAGE).forward(request, response);
    }

    private static void setUsersAttribute(final HttpServletRequest request, final Collection<User> users) {
        request.setAttribute(RequestAttributes.USERS, users);
    }
}
