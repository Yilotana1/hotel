package com.example.hotel.controller.action;

import com.example.hotel.controller.commons.Constants.RequestParameters;
import com.example.hotel.controller.commons.Path;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;

import static com.example.hotel.controller.commons.Tools.deleteFromLoginCache;


/**
 * Delete user data from session and login cache and makes redirect to main page.
 * After that user cannot make any orders.
 */
public class LogOutAction implements Action {
    public final static Logger log = Logger.getLogger(LogOutAction.class);
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
