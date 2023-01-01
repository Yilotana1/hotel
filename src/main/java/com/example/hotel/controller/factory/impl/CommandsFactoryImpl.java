package com.example.hotel.controller.factory.impl;

import com.example.hotel.commons.Path;
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
import com.example.hotel.controller.command.client.UpdateUserMoneyCommand;
import com.example.hotel.controller.command.manager.ApplyForClientCommand;
import com.example.hotel.controller.command.manager.ShowApartmentsCommand;
import com.example.hotel.controller.command.manager.ShowPreferredApartmentsCommand;
import com.example.hotel.controller.command.manager.ShowTemporaryApplicationsCommand;
import com.example.hotel.controller.command.manager.ShowUsersCommand;
import com.example.hotel.controller.factory.CommandsFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandsFactoryImpl extends CommandsFactory {
    private final Map<String, Command> getCommands = new HashMap<>();
    private final Map<String, Command> postCommands = new HashMap<>();
    public CommandsFactoryImpl() {
        setGetUriToCommands();
        setPostUriToCommands();
    }

    @Override
    public Command getCommand(final String uri) {
        if (getCommands.containsKey(uri)) {
            return getCommands.get(uri);
        }
        if (postCommands.containsKey(uri)) {
            return postCommands.get(uri);
        }
        return null;
    }

    private void setPostUriToCommands() {
        postCommands.put(Path.Post.User.SIGNUP, new SignUpCommand());
        postCommands.put(Path.Post.User.EDIT_PROFILE, new EditProfileCommand());
        postCommands.put(Path.Post.Admin.ADMIN_EDIT_USER, new EditUserCommand());
        postCommands.put(Path.Post.Admin.EDIT_APARTMENT, new EditApartmentCommand());
        postCommands.put(Path.Post.Client.UPDATE_MONEY_ACCOUNT, new UpdateUserMoneyCommand());
        postCommands.put(Path.Post.Client.CLIENT_APPLY, new ApplyCommand());
        postCommands.put(Path.Post.Client.CONFIRM_PAYMENT, new ConfirmPaymentCommand());
        postCommands.put(Path.Post.Client.CANCEL_APPLICATION, new CancelApplicationCommand());
        postCommands.put(Path.Post.Client.MAKE_TEMPORARY_APPLICATION, new MakeTemporaryApplicationCommand());
        postCommands.put(Path.Post.Manager.APPLY_FOR_CLIENT, new ApplyForClientCommand());
    }

    private void setGetUriToCommands() {
        getCommands.put(Path.Get.User.LOGIN, new LoginCommand());
        getCommands.put(Path.Get.User.LOGOUT, new LogOutCommand());
        getCommands.put(Path.Get.User.MAIN, new MainCommand());
        getCommands.put(Path.Get.User.PROFILE, new ShowProfileCommand());
        getCommands.put(Path.Get.Admin.ADMIN_MANAGE_USERS, new ShowUsersManagementCommand());
        getCommands.put(Path.Get.Manager.MANAGER_LIST_USERS, new ShowUsersCommand());
        getCommands.put(Path.Get.Client.APPLICATION_INVOICE, new ShowApplicationInvoiceCommand());
        getCommands.put(Path.Get.Manager.SHOW_PREFERRED_APARTMENTS, new ShowPreferredApartmentsCommand());
        getCommands.put(Path.Get.Manager.SHOW_TEMPORARY_APPLICATIONS, new ShowTemporaryApplicationsCommand());
        getCommands.put(Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT, new ShowApartmentsManagementCommand());
        getCommands.put(Path.Get.Manager.SHOW_APARTMENTS, new ShowApartmentsCommand());
    }
}
