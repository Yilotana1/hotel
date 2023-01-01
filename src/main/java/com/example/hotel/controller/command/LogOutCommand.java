package com.example.hotel.controller.command;

import com.example.hotel.commons.Constants.RequestParameters;
import com.example.hotel.commons.Path;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.commons.Tools.deleteFromLoginCache;


/**
 * Delete user data from session and login cache and makes redirect to main page.
 * After that user cannot make any orders.
 */
public class LogOutCommand implements Command {
    public final static Logger log = Logger.getLogger(LogOutCommand.class);
    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        try {
            log.debug("Command started executing");

            request.getSession().invalidate();
            deleteFromLoginCache(request, request.getParameter(RequestParameters.LOGIN));
            response.sendRedirect(request.getContextPath() + Path.Get.User.MAIN);

            log.debug("Logout passed successfully");
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            response.sendRedirect(request.getContextPath() + Path.Get.Error.ERROR_503);
        }
    }
}
