package com.example.hotel.controller;

import com.example.hotel.controller.factory.action.ActionFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;

import static com.example.hotel.controller.commons.Constants.BASE_URL;
import static com.example.hotel.controller.commons.Constants.ServletContextAttributes.LOGGED_USERS;

public class Controller extends HttpServlet {
  public static final Logger log = Logger.getLogger(Controller.class);
  private final ActionFactory actionFactory = ActionFactory.getInstance();

  public void init() {
    getServletConfig()
            .getServletContext()
            .setAttribute(LOGGED_USERS, new HashSet<String>());
  }

  public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
    processRequest(req, resp);
    log.trace("doGet finished; request URI = " + req.getRequestURI());
  }

  public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
    processRequest(req, resp);
    log.trace("doPost finished; Request URI = " + req.getRequestURI());
  }

  private void processRequest(
          final HttpServletRequest req,
          final HttpServletResponse resp) throws ServletException, IOException {
    var path = req.getRequestURI();
    log.trace("Received URI from request: " + path);
    path = path.replaceAll(".*" + BASE_URL, "");
    final var action = actionFactory.getAction(path);
    action.execute(req, resp);
  }
}