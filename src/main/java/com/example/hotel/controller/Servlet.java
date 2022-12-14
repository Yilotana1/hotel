package com.example.hotel.controller;

import com.example.hotel.commons.Constants.ServletContextAttributes;
import com.example.hotel.controller.factory.CommandsFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;

import static java.util.ResourceBundle.getBundle;

public class Servlet extends HttpServlet {
    public static final Logger log = Logger.getLogger(Servlet.class);
    private static final String BASE_URL = getBundle("application").getString("base_url");
    private final CommandsFactory commandsFactory = CommandsFactory.getInstance();

    public void init() {
        getServletConfig().getServletContext()
                .setAttribute(ServletContextAttributes.LOGGED_USERS, new HashSet<String>());
    }

    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        processRequest(req, resp);
        log.trace("doGet finished");
    }

    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        processRequest(req, resp);
        log.trace("doGet finished");
    }

    private void processRequest(
            final HttpServletRequest req,
            final HttpServletResponse resp) throws ServletException, IOException {
        var path = req.getRequestURI();

        log.trace("Received URI from request: " + path);

        path = path.replaceAll(".*" + BASE_URL, "");
        final var command = commandsFactory.getCommand(path);
        command.execute(req, resp);
    }
}

//TODO Put all urls in Path interface into properties (watch ResourceBundle)
//TODO Create new folders for jsp pages and make it look more structured. For example put all error pages of client into folder /client/error/...
//TODO Make logs look more formal.
//TODO Create at least a few mock-tests.
//TODO Generate Create-sql-script of the finale database.
//TODO Fix bug. hasNotAllowedNulls() method in dto's must throw InvalidDateException with specified wrongField[ALL fields or something]