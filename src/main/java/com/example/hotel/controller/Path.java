package com.example.hotel.controller;

public abstract class Path {

    public static abstract class Get {
        public static abstract class Client {
            public static final String CLIENT_APPLY_PAGE = "/client/apply.jsp";
            public static final String SHOW_APPLY_PAGE = "/client/show-application-page";
            public static final String SUCCESS_APPLY_PAGE = "/client/success_apply.jsp";
            public static final String APPLICATION_INVOICE_PAGE = "/client/application-invoice.jsp";
            public static final String APPLICATION_INVOICE = "/client/application-invoice";
            public static final String APARTMENT_NOT_AVAILABLE_PAGE = "/client/apartment-not-available.jsp";
            public static final String CLIENT_HAS_APPLICATION_PAGE = "/client/client-has-application.jsp";

            public static final String MONEY_VALUE_IS_INCORRECT = "/client/wrong-money-value-exception.jsp";

            public static final String APPLICATION_CANCELED = "/client/application-canceled.jsp";

            public static final String MAKE_TEMPORARY_APPLICATION_PAGE = "/client/make-temporary-application.jsp";
            public static final String WRONG_STAY_LENGTH = "/client/wrong-stay-length.jsp";

        }

        public abstract static class Manager {
            public static final String PREFERRED_APARTMENTS_PAGE = "/manager/preferred-apartments.jsp";
            public static final String SHOW_PREFERRED_APARTMENTS = "/manager/show-preferred-apartments";
            public static final String CLIENT_HAS_ASSIGNED_APPLICATION_PAGE = "/manager/client-has-assigned-application.jsp";
            public static final String SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE = "/manager/successful-application-assignment.jsp";

            public static final String SHOW_TEMPORARY_APPLICATIONS = "/manager/show-temporary-applications";
            public static final String TEMPORARY_APPLICATIONS_PAGE = "/manager/temporary-applications.jsp";
            public static final String APARTMENTS_PAGE = "/manager/apartments.jsp";
            public static final String SHOW_APARTMENTS = "/manager/show-apartments";
            public static final String MANAGER_LIST_USERS_PAGE = "/manager/list_users.jsp";
            public static final String MANAGER_LIST_USERS = "/manager/list-users";

        }

        public abstract static class Admin {
            public static final String ADMIN_MANAGE_USERS_PAGE = "/admin/manage_users.jsp";
            public static final String ADMIN_MANAGE_USERS = "/admin/manage-users";
            public static final String SHOW_APARTMENTS_MANAGEMENT = "/admin/show-apartment-management";
            public static final String APARTMENTS_MANAGEMENT_PAGE = "/admin/manage-apartments.jsp";

        }

        public abstract static class User {
            public static final String SIGNUP_PAGE = "/signup.jsp";
            public static final String MAIN_PAGE = "/main.jsp";
            public static final String LOGIN = "/login";
            public static final String LOGIN_PAGE = "/login.jsp";
            public static final String LOGOUT = "/logout";
            public static final String MAIN = "/main";
            public static final String ERROR_404_PAGE = "/404error.jsp";
            public static final String ERROR_503_PAGE = "/503error.jsp";
            public static final String PROFILE = "/profile";
            public static final String PROFILE_PAGE = "/profile.jsp";
        }
    }

    public static abstract class Post {
        public static abstract class Client {
            public static final String CLIENT_APPLY = "/client/apply";
            public static final String CONFIRM_PAYMENT = "/client/confirm-payment";
            public static final String UPDATE_MONEY_ACCOUNT = "/client/update-money";
            public static final String CANCEL_APPLICATION = "/client/cancel-application";
            public static final String MAKE_TEMPORARY_APPLICATION = "/client/make-temporary-application";
        }

        public static abstract class Manager {
            public static final String APPLY_FOR_CLIENT = "/manager/apply-for-client";
        }

        public static abstract class Admin {
            public static final String ADMIN_EDIT_USER = "/admin/edit-user";
            public static final String EDIT_APARTMENT = "/admin/edit-apartment";
        }

        public static abstract class User {
            public static final String SIGNUP = "/signup";
            public static final String EDIT_PROFILE = "/edit-profile";
        }
    }
}
