package com.example.hotel.controller.command.admin;

import com.example.hotel.controller.command.Command;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.example.hotel.controller.Path.ADMIN_MANAGE_USERS_PAGE;
import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ShowUsersManagementCommand implements Command {

    public static final Logger log = Logger.getLogger(ShowUsersManagementCommand.class);

    private UserService userService = ServiceFactory.getInstance().createUserService();
    private static final String PAGE_NUMBER_INPUT = "page";
    private static final String DEFAULT_PAGE_NUMBER = "1";
    private static final int PAGE_SIZE = 10;
    private static final String USERS = "users";
    private static final String TOTAL_PAGES_NUMBER = "count";


    public ShowUsersManagementCommand() {
    }

    public ShowUsersManagementCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageNumber = parseInt(requireNonNullElse(request.getParameter(PAGE_NUMBER_INPUT), DEFAULT_PAGE_NUMBER));
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            final var login = request.getParameter("login");
            if (login != null && !login.isEmpty()) {
                final var user = userService.getByLogin(login);
                user.ifPresent(u -> request.setAttribute(USERS, List.of(u)));
                request.setAttribute(TOTAL_PAGES_NUMBER, user.map(u -> 1).orElse(0));
            } else {
                final var users = userService.getUsers(skip, PAGE_SIZE);
                request.setAttribute(USERS, users);
                final var totalPagesNumber = userService.count() / PAGE_SIZE;
                request.setAttribute(TOTAL_PAGES_NUMBER, totalPagesNumber);
            }
        } catch (final Exception e) {
            log.error(e.getMessage());
            response.sendRedirect(request.getContextPath() + ERROR_503_PAGE);
        }
        request.getRequestDispatcher(ADMIN_MANAGE_USERS_PAGE).forward(request, response);
    }
}
