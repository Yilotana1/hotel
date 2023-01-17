package com.example.hotel.controller.factory.impl;

import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.command.Action;
import com.example.hotel.controller.command.EditProfileAction;
import com.example.hotel.controller.command.LogOutAction;
import com.example.hotel.controller.command.LoginAction;
import com.example.hotel.controller.command.MainAction;
import com.example.hotel.controller.command.ShowProfileAction;
import com.example.hotel.controller.command.SignUpAction;
import com.example.hotel.controller.command.admin.EditApartmentAction;
import com.example.hotel.controller.command.admin.EditUserAction;
import com.example.hotel.controller.command.admin.ShowApartmentsManagementAction;
import com.example.hotel.controller.command.admin.ShowUsersManagementAction;
import com.example.hotel.controller.command.client.ApplyAction;
import com.example.hotel.controller.command.client.CancelApplicationAction;
import com.example.hotel.controller.command.client.ConfirmPaymentAction;
import com.example.hotel.controller.command.client.MakeTemporaryApplicationAction;
import com.example.hotel.controller.command.client.ShowApplicationInvoiceAction;
import com.example.hotel.controller.command.client.UpdateUserMoneyAction;
import com.example.hotel.controller.command.manager.ApplyForClientAction;
import com.example.hotel.controller.command.manager.ShowApartmentsAction;
import com.example.hotel.controller.command.manager.ShowPreferredApartmentsAction;
import com.example.hotel.controller.command.manager.ShowTemporaryApplicationsAction;
import com.example.hotel.controller.command.manager.ShowUsersAction;
import com.example.hotel.controller.factory.CommandsFactory;

import java.util.HashMap;
import java.util.Map;

public class CommandsFactoryImpl extends CommandsFactory {
    private final Map<String, Action> getCommands = new HashMap<>();
    private final Map<String, Action> postCommands = new HashMap<>();
    public CommandsFactoryImpl() {
        setGetUriToCommands();
        setPostUriToCommands();
    }

    @Override
    public Action getCommand(final String uri) {
        if (getCommands.containsKey(uri)) {
            return getCommands.get(uri);
        }
        if (postCommands.containsKey(uri)) {
            return postCommands.get(uri);
        }
        return null;
    }

    private void setPostUriToCommands() {
        postCommands.put(Path.Post.User.SIGNUP, new SignUpAction());
        postCommands.put(Path.Post.User.EDIT_PROFILE, new EditProfileAction());
        postCommands.put(Path.Post.Admin.ADMIN_EDIT_USER, new EditUserAction());
        postCommands.put(Path.Post.Admin.EDIT_APARTMENT, new EditApartmentAction());
        postCommands.put(Path.Post.Client.UPDATE_MONEY_ACCOUNT, new UpdateUserMoneyAction());
        postCommands.put(Path.Post.Client.CLIENT_APPLY, new ApplyAction());
        postCommands.put(Path.Post.Client.CONFIRM_PAYMENT, new ConfirmPaymentAction());
        postCommands.put(Path.Post.Client.CANCEL_APPLICATION, new CancelApplicationAction());
        postCommands.put(Path.Post.Client.MAKE_TEMPORARY_APPLICATION, new MakeTemporaryApplicationAction());
        postCommands.put(Path.Post.Manager.APPLY_FOR_CLIENT, new ApplyForClientAction());
    }

    private void setGetUriToCommands() {
        getCommands.put(Path.Get.User.LOGIN, new LoginAction());
        getCommands.put(Path.Get.User.LOGOUT, new LogOutAction());
        getCommands.put(Path.Get.User.MAIN, new MainAction());
        getCommands.put(Path.Get.User.PROFILE, new ShowProfileAction());
        getCommands.put(Path.Get.Admin.ADMIN_MANAGE_USERS, new ShowUsersManagementAction());
        getCommands.put(Path.Get.Manager.MANAGER_LIST_USERS, new ShowUsersAction());
        getCommands.put(Path.Get.Client.APPLICATION_INVOICE, new ShowApplicationInvoiceAction());
        getCommands.put(Path.Get.Manager.SHOW_PREFERRED_APARTMENTS, new ShowPreferredApartmentsAction());
        getCommands.put(Path.Get.Manager.SHOW_TEMPORARY_APPLICATIONS, new ShowTemporaryApplicationsAction());
        getCommands.put(Path.Get.Admin.SHOW_APARTMENTS_MANAGEMENT, new ShowApartmentsManagementAction());
        getCommands.put(Path.Get.Manager.SHOW_APARTMENTS, new ShowApartmentsAction());
    }
}
