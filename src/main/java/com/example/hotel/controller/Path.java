package com.example.hotel.controller;

public interface Path {
    String SIGNUP = "/signup";
    String SIGNUP_PAGE = "/signup.jsp";
    String MAIN_PAGE = "/main.jsp";
    String LOGIN = "/login";
    String LOGIN_PAGE = "/login.jsp";
    String LOGOUT = "/logout";
    String MAIN = "/main";
    String ERROR_404_PAGE = "/404error.jsp";
    String ERROR_503_PAGE = "/503error.jsp";
    String PROFILE = "/profile";
    String PROFILE_PAGE = "/profile.jsp";
    String EDIT_PROFILE = "/edit-profile";
    String ADMIN_MANAGE_USERS = "/admin/manage-users";
    String ADMIN_MANAGE_USERS_PAGE = "/admin/manage_users.jsp";
    String ADMIN_EDIT_USER = "/admin/edit-user";
    String MANAGER_LIST_USERS_PAGE = "/manager/list_users.jsp";
    String MANAGER_LIST_USERS = "/manager/list-users";
    String UPDATE_MONEY_ACCOUNT = "/client/update-money";
    String CLIENT_APPLY = "/client/apply";
    String CLIENT_APPLY_PAGE = "/client/apply.jsp";
    String SHOW_APPLY_PAGE = "/client/show-application-page";
    String SUCCESS_APPLY_PAGE = "/client/success_apply.jsp";
    String APPLICATION_INVOICE_PAGE = "/client/application-invoice.jsp";
    String APPLICATION_INVOICE = "/client/application-invoice";
    String APARTMENT_NOT_AVAILABLE_PAGE = "/client/apartment-not-available.jsp";
    String CLIENT_HAS_APPLICATION_PAGE = "/client/client-has-application.jsp";
    String CONFIRM_PAYMENT = "/client/confirm-payment";
    String MONEY_VALUE_IS_INCORRECT = "/client/wrong-money-value-exception.jsp";
    String CANCEL_APPLICATION = "/client/cancel-application";
    String APPLICATION_CANCELED = "/client/application-canceled.jsp";
    String MAKE_TEMPORARY_APPLICATION = "/client/make-temporary-application";
    String MAKE_TEMPORARY_APPLICATION_PAGE = "/client/make-temporary-application.jsp";
    String PREFERRED_APARTMENTS_PAGE = "/manager/preferred-apartments.jsp";
    String SHOW_PREFERRED_APARTMENTS = "/manager/show-preferred-apartments";
    String CLIENT_HAS_ASSIGNED_APPLICATION_PAGE = "/manager/client-has-assigned-application.jsp";
    String SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE = "/manager/successful-application-assignment.jsp";
    String APPLY_FOR_CLIENT = "/manager/apply-for-client";
    String SHOW_TEMPORARY_APPLICATIONS = "/manager/show-temporary-applications";
    String TEMPORARY_APPLICATIONS_PAGE = "/manager/temporary-applications.jsp";
    String SHOW_APARTMENTS_MANAGEMENT = "/admin/show-apartment-management";
    String APARTMENTS_MANAGEMENT_PAGE = "/admin/manage-apartments.jsp";
    String APARTMENTS_PAGE = "/manager/apartments.jsp";
    String SHOW_APARTMENTS = "/manager/show-apartments";
    String EDIT_APARTMENT = "/admin/edit-apartment";
}
