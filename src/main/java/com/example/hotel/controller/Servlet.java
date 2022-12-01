package com.example.hotel.controller;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.LogOutCommand;
import com.example.hotel.controller.command.LoginCommand;
import com.example.hotel.controller.command.SignUpCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.example.hotel.controller.Path.LOGIN;
import static com.example.hotel.controller.Path.LOGOUT;
import static com.example.hotel.controller.Path.SIGNUP;

public class Servlet extends HttpServlet {
    private final Map<String, Command> commands = new HashMap<>();

    public final static Logger log = Logger.getLogger(Servlet.class);

    public void init() {
        getServletConfig().getServletContext()
                .setAttribute("loggedUsers", new HashSet<String>());

        commands.put(SIGNUP, new SignUpCommand());
        commands.put(LOGIN, new LoginCommand());
        commands.put(LOGOUT, new LogOutCommand());
    }


//   TODO Make validation errors to be shown in jsp form
    //TODO Make LoginCommand as well as SignUpCommand previously
    //TODO Make main page with login and signup button
    //TODO Make basic localization
    //TODO Make basic expiring listener
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        var path = req.getRequestURI();

        log.trace("Received URI from request: " + path);

        path = path.replaceAll(".*/hotel_war_exploded", "");
        var command = commands.getOrDefault(path,
                null);
        command.execute(req, resp);

        log.trace("doGet finished");
    }

    public void destroy() {
    }
}