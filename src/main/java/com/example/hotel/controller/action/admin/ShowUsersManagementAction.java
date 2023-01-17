package com.example.hotel.controller.action.admin;

import com.example.hotel.controller.commons.Constants.RequestAttributes;
import com.example.hotel.controller.commons.Constants.RequestAttributes.PaginationAttributes;
import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.action.Action;
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

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ShowUsersManagementAction implements Action {

    public static final Logger log = Logger.getLogger(ShowUsersManagementAction.class);
    private UserService userService = ServiceFactory.getInstance().createUserService();
    private static final int PAGE_SIZE = 10;
    public ShowUsersManagementAction() {
    }

    public ShowUsersManagementAction(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var page = getPageId(request);
            final var skip = (page - 1) * PAGE_SIZE;
            final var login = request.getParameter(RequestParameters.LOGIN);
            if (login == null || login.isEmpty()) {
                final var users = userService.getUsers(skip, PAGE_SIZE);
                setUsersAttribute(request, users);
                final var totalPagesNumber = userService.count() / PAGE_SIZE;
                setFinalAttributes(request, totalPagesNumber);
                request.getRequestDispatcher(Path.Get.Admin.ADMIN_MANAGE_USERS_PAGE).forward(request, response);
                return;
            }
            final var user = userService.getByLogin(login);
            user.ifPresent(u -> setUsersAttribute(request, List.of(u)));
            final var totalPageNumber = user.map(u -> 1).orElse(0);
            setFinalAttributes(request, totalPageNumber);
            request.getRequestDispatcher(Path.Get.Admin.ADMIN_MANAGE_USERS_PAGE).forward(request, response);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }

    }

    private void setUsersAttribute(final HttpServletRequest request, final Collection<User> users) {
        request.setAttribute(RequestAttributes.USERS, users);
    }

    private void setFinalAttributes(final HttpServletRequest request,
                                    final Integer totalPageNumber) {
        request.setAttribute(PaginationAttributes.TOTAL_PAGES_NUMBER, totalPageNumber);
    }

    private int getPageId(final HttpServletRequest request) {
        final var pageInput = request.getParameter(PaginationAttributes.PAGE);
        final var page = requireNonNullElse(pageInput, PaginationAttributes.DEFAULT_PAGE);
        return parseInt(page);
    }
}
