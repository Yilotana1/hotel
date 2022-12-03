package com.example.hotel.controller.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.Path.MAIN;
import static com.example.hotel.model.dao.Tools.deleteFromLoginCache;


/**
 * Delete user data from session and login cache and makes redirect to main page.
 * After that user cannot make any orders.
 */
public class LogOutCommand implements Command {
    public final static Logger log = Logger.getLogger(LogOutCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("Command started executing");

        request.getSession().invalidate();
        deleteFromLoginCache(request, request.getParameter("login"));
        response.sendRedirect(request.getContextPath() + MAIN);

        log.debug("Logout passed successfully");
    }
}
