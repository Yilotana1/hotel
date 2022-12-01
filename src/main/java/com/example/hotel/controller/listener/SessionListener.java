package com.example.hotel.controller.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;

import java.util.HashSet;


public class SessionListener implements HttpSessionListener {

    public static final Logger log = Logger.getLogger(SessionListener.class);

    public static final int INACTIVE_INTERVAL = 20 * 60;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setMaxInactiveInterval(INACTIVE_INTERVAL);
        log.debug("Session is created: maxInactiveInterval = " + INACTIVE_INTERVAL);

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        var loggedUsers = (HashSet<String>) httpSessionEvent
                .getSession().getServletContext()
                .getAttribute("loggedUsers");


        var login = ((String) httpSessionEvent.getSession().getAttribute("login"));
        loggedUsers.remove(login);
        log.trace("User's login has been removed from login cache, user's login = " + login);

        log.debug("Session is destroyed");
    }
}
