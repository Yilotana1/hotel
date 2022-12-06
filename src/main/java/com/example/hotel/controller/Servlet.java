package com.example.hotel.controller;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.EditProfileCommand;
import com.example.hotel.controller.command.LogOutCommand;
import com.example.hotel.controller.command.LoginCommand;
import com.example.hotel.controller.command.MainCommand;
import com.example.hotel.controller.command.ShowProfileCommand;
import com.example.hotel.controller.command.SignUpCommand;
import com.example.hotel.controller.command.admin.EditUserCommand;
import com.example.hotel.controller.command.admin.ManageUsersCommand;
import com.example.hotel.controller.command.manager.ListUsersCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.example.hotel.controller.Path.ADMIN_EDIT_USER;
import static com.example.hotel.controller.Path.ADMIN_MANAGE_USERS;
import static com.example.hotel.controller.Path.EDIT_PROFILE;
import static com.example.hotel.controller.Path.LOGIN;
import static com.example.hotel.controller.Path.LOGOUT;
import static com.example.hotel.controller.Path.MAIN;
import static com.example.hotel.controller.Path.MANAGER_LIST_USERS;
import static com.example.hotel.controller.Path.PROFILE;
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
        commands.put(MAIN, new MainCommand());
        commands.put(PROFILE, new ShowProfileCommand());
        commands.put(EDIT_PROFILE, new EditProfileCommand());
        commands.put(ADMIN_MANAGE_USERS, new ManageUsersCommand());
        commands.put(ADMIN_EDIT_USER, new EditUserCommand());
        commands.put(MANAGER_LIST_USERS, new ListUsersCommand());
    }

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