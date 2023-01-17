package com.example.hotel.controller;

import com.example.hotel.controller.commons.Constants.ServletContextAttributes;
import com.example.hotel.controller.factory.CommandsFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;

import static com.example.hotel.controller.commons.Constants.BASE_URL;

public class Controller extends HttpServlet {
    public static final Logger log = Logger.getLogger(Controller.class);
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
        log.info(req.getContextPath());
        log.trace("Received URI from request: " + path);

        path = path.replaceAll(".*" + BASE_URL, "");
        final var command = commandsFactory.getCommand(path);
        command.execute(req, resp);
    }
}