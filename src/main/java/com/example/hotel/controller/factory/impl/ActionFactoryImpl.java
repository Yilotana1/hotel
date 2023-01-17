package com.example.hotel.controller.factory.impl;

import com.example.hotel.controller.commons.Path;
import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.action.EditProfileAction;
import com.example.hotel.controller.action.LogOutAction;
import com.example.hotel.controller.action.LoginAction;
import com.example.hotel.controller.action.MainAction;
import com.example.hotel.controller.action.ShowProfileAction;
import com.example.hotel.controller.action.SignUpAction;
import com.example.hotel.controller.action.admin.EditApartmentAction;
import com.example.hotel.controller.action.admin.EditUserAction;
import com.example.hotel.controller.action.admin.ShowApartmentsManagementAction;
import com.example.hotel.controller.action.admin.ShowUsersManagementAction;
import com.example.hotel.controller.action.client.ApplyAction;
import com.example.hotel.controller.action.client.CancelApplicationAction;
import com.example.hotel.controller.action.client.ConfirmPaymentAction;
import com.example.hotel.controller.action.client.MakeTemporaryApplicationAction;
import com.example.hotel.controller.action.client.ShowApplicationInvoiceAction;
import com.example.hotel.controller.action.client.UpdateUserMoneyAction;
import com.example.hotel.controller.action.manager.ApplyForClientAction;
import com.example.hotel.controller.action.manager.ShowApartmentsAction;
import com.example.hotel.controller.action.manager.ShowPreferredApartmentsAction;
import com.example.hotel.controller.action.manager.ShowTemporaryApplicationsAction;
import com.example.hotel.controller.action.manager.ShowUsersAction;
import com.example.hotel.controller.factory.ActionFactory;

import java.util.HashMap;
import java.util.Map;

public class ActionFactoryImpl extends ActionFactory {
    private final Map<String, Action> getCommands = new HashMap<>();
    private final Map<String, Action> postCommands = new HashMap<>();
    public ActionFactoryImpl() {
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
