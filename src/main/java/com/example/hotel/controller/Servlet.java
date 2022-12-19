package com.example.hotel.controller;

import com.example.hotel.controller.command.Command;
import com.example.hotel.controller.command.EditProfileCommand;
import com.example.hotel.controller.command.LogOutCommand;
import com.example.hotel.controller.command.LoginCommand;
import com.example.hotel.controller.command.MainCommand;
import com.example.hotel.controller.command.ShowProfileCommand;
import com.example.hotel.controller.command.SignUpCommand;
import com.example.hotel.controller.command.admin.EditApartmentCommand;
import com.example.hotel.controller.command.admin.EditUserCommand;
import com.example.hotel.controller.command.admin.ShowApartmentsManagementCommand;
import com.example.hotel.controller.command.admin.ShowUsersManagementCommand;
import com.example.hotel.controller.command.client.ApplyCommand;
import com.example.hotel.controller.command.client.CancelApplicationCommand;
import com.example.hotel.controller.command.client.ConfirmPaymentCommand;
import com.example.hotel.controller.command.client.MakeTemporaryApplicationCommand;
import com.example.hotel.controller.command.client.ShowApplicationInvoiceCommand;
import com.example.hotel.controller.command.client.ShowApplicationPageCommand;
import com.example.hotel.controller.command.client.UpdateUserMoneyCommand;
import com.example.hotel.controller.command.manager.ApplyForClientCommand;
import com.example.hotel.controller.command.manager.ShowApartmentsCommand;
import com.example.hotel.controller.command.manager.ShowPreferredApartmentsCommand;
import com.example.hotel.controller.command.manager.ShowTemporaryApplicationsCommand;
import com.example.hotel.controller.command.manager.ShowUsersCommand;
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
import static com.example.hotel.controller.Path.APPLICATION_INVOICE;
import static com.example.hotel.controller.Path.APPLY_FOR_CLIENT;
import static com.example.hotel.controller.Path.CANCEL_APPLICATION;
import static com.example.hotel.controller.Path.CLIENT_APPLY;
import static com.example.hotel.controller.Path.CONFIRM_PAYMENT;
import static com.example.hotel.controller.Path.EDIT_APARTMENT;
import static com.example.hotel.controller.Path.EDIT_PROFILE;
import static com.example.hotel.controller.Path.LOGIN;
import static com.example.hotel.controller.Path.LOGOUT;
import static com.example.hotel.controller.Path.MAIN;
import static com.example.hotel.controller.Path.MAKE_TEMPORARY_APPLICATION;
import static com.example.hotel.controller.Path.MANAGER_LIST_USERS;
import static com.example.hotel.controller.Path.PROFILE;
import static com.example.hotel.controller.Path.SHOW_APARTMENTS;
import static com.example.hotel.controller.Path.SHOW_APARTMENTS_MANAGEMENT;
import static com.example.hotel.controller.Path.SHOW_APPLY_PAGE;
import static com.example.hotel.controller.Path.SHOW_PREFERRED_APARTMENTS;
import static com.example.hotel.controller.Path.SHOW_TEMPORARY_APPLICATIONS;
import static com.example.hotel.controller.Path.SIGNUP;
import static com.example.hotel.controller.Path.UPDATE_MONEY_ACCOUNT;

public class Servlet extends HttpServlet {
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, Command> postCommands = new HashMap<>();

    public final static Logger log = Logger.getLogger(Servlet.class);

    public void init() {
        getServletConfig().getServletContext()
                .setAttribute("loggedUsers", new HashSet<String>());
//GET
        commands.put(LOGIN, new LoginCommand());
        commands.put(LOGOUT, new LogOutCommand());
        commands.put(MAIN, new MainCommand());
        commands.put(PROFILE, new ShowProfileCommand());
        commands.put(ADMIN_MANAGE_USERS, new ShowUsersManagementCommand());
        commands.put(MANAGER_LIST_USERS, new ShowUsersCommand());
        commands.put(SHOW_APPLY_PAGE, new ShowApplicationPageCommand());
        commands.put(APPLICATION_INVOICE, new ShowApplicationInvoiceCommand());
        commands.put(SHOW_PREFERRED_APARTMENTS, new ShowPreferredApartmentsCommand());
        commands.put(SHOW_TEMPORARY_APPLICATIONS, new ShowTemporaryApplicationsCommand());
        commands.put(SHOW_APARTMENTS_MANAGEMENT, new ShowApartmentsManagementCommand());
        commands.put(SHOW_APARTMENTS, new ShowApartmentsCommand());
//POST
        postCommands.put(SIGNUP, new SignUpCommand());
        postCommands.put(EDIT_PROFILE, new EditProfileCommand());
        postCommands.put(ADMIN_EDIT_USER, new EditUserCommand());
        postCommands.put(UPDATE_MONEY_ACCOUNT, new UpdateUserMoneyCommand());
        postCommands.put(CLIENT_APPLY, new ApplyCommand());
        postCommands.put(CONFIRM_PAYMENT, new ConfirmPaymentCommand());
        postCommands.put(CANCEL_APPLICATION, new CancelApplicationCommand());
        postCommands.put(MAKE_TEMPORARY_APPLICATION, new MakeTemporaryApplicationCommand());
        postCommands.put(APPLY_FOR_CLIENT, new ApplyForClientCommand());
        postCommands.put(EDIT_APARTMENT, new EditApartmentCommand());
    }

    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        var path = req.getRequestURI();

        log.trace("Received URI from request: " + path);

        path = path.replaceAll(".*/hotel_war_exploded", "");
        final var command = commands.getOrDefault(path,
                null);
        command.execute(req, resp);

        log.trace("doGet finished");
    }

    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException, ServletException {
        var path = req.getRequestURI();

        log.trace("Received URI from request: " + path);

        path = path.replaceAll(".*/hotel_war_exploded", "");
        final var command = postCommands.getOrDefault(path, null);
        command.execute(req, resp);

        log.trace("doGet finished");
    }

    public void destroy() {
    }
}

//TODO Put all urls in Path interface into properties (watch ResourceBundle)
//TODO Create new folders for jsp pages and make it look more structured. For example put all error pages of client into folder /client/error/...
//TODO Make logs look more formal.
//TODO Create at least a few mock-tests.
//TODO Generate Create-sql-script of the finale database.
//TODO Fix bug. hasNotAllowedNulls() method in dto's must throw InvalidDateException with specified wrongField[ALL fields or something]