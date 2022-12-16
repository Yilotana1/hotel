package com.example.hotel.controller.command.manager;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.admin.ShowUsersManagmentCommand;
import com.example.hotel.model.service.UserService;
import com.example.hotel.model.service.exception.ServiceException;
import com.example.hotel.model.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static com.example.hotel.controller.Path.ERROR_503_PAGE;
import static com.example.hotel.controller.Path.MANAGER_LIST_USERS_PAGE;
import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNullElse;

public class ListUsersCommand implements Command {
    public final static Logger log = Logger.getLogger(ShowUsersManagmentCommand.class);

    private UserService userService = ServiceFactory.getInstance().createUserService();
    private final String PAGE_NUMBER_INPUT = "page";
    private final String DEFAULT_PAGE_NUMBER = "1";
    private final int PAGE_SIZE = 10;
    private final String USERS = "users";
    private static final String TOTAL_PAGES_NUMBER = "count";


    public ListUsersCommand() {
    }

    public ListUsersCommand(final ServiceFactory serviceFactory) {
        userService = serviceFactory.createUserService();
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            final var pageNumber = parseInt(requireNonNullElse(request.getParameter(PAGE_NUMBER_INPUT), DEFAULT_PAGE_NUMBER));
            final var skip = (pageNumber - 1) * PAGE_SIZE;
            if (request.getParameter("by_login") != null) {
                final var login = request.getParameter("login");
                final var user = userService.getByLogin(login);
                user.ifPresent(u -> request.setAttribute(USERS, List.of(u)));
                request.setAttribute(TOTAL_PAGES_NUMBER, user.map(u -> 1).orElse(0));
            } else {
                final var users = userService.getUsers(skip, PAGE_SIZE);
                request.setAttribute(USERS, users);
                final var totalPagesNumber = userService.count() / PAGE_SIZE;
                request.setAttribute(TOTAL_PAGES_NUMBER, totalPagesNumber);
            }
        } catch (final ServiceException e) {
            log.error(e.getMessage());
            request.getRequestDispatcher(ERROR_503_PAGE).forward(request, response);
        }
        request.getRequestDispatcher(MANAGER_LIST_USERS_PAGE).forward(request, response);
    }
}
