package com.example.hotel.controller.commons;

import java.util.ResourceBundle;

public abstract class Path {
    private static final ResourceBundle ADMIN_GET_PROPERTIES = ResourceBundle.getBundle("uri/get/admin");
    private static final ResourceBundle CLIENT_GET_PROPERTIES = ResourceBundle.getBundle("uri/get/client");
    private static final ResourceBundle USER_GET_PROPERTIES = ResourceBundle.getBundle("uri/get/user");
    private static final ResourceBundle MANAGER_GET_PROPERTIES = ResourceBundle.getBundle("uri/get/manager");

    private static final ResourceBundle ADMIN_POST_PROPERTIES = ResourceBundle.getBundle("uri/post/admin");
    private static final ResourceBundle CLIENT_POST_PROPERTIES = ResourceBundle.getBundle("uri/post/client");
    private static final ResourceBundle USER_POST_PROPERTIES = ResourceBundle.getBundle("uri/post/user");
    private static final ResourceBundle MANAGER_POST_PROPERTIES = ResourceBundle.getBundle("uri/post/manager");
    private static final ResourceBundle ERROR_GET_PROPERTIES = ResourceBundle.getBundle("uri/get/error");

    public static abstract class Get {
        public static abstract class Client {
            public static final String SHOW_APPLY_PAGE = CLIENT_GET_PROPERTIES
                    .getString("show_apply_page");

            public static final String SUCCESS_APPLY_PAGE = CLIENT_GET_PROPERTIES
                    .getString("success_apply_page");

            public static final String APPLICATION_INVOICE = CLIENT_GET_PROPERTIES
                    .getString("show_application_invoice");

            public static final String APARTMENT_NOT_AVAILABLE_PAGE = CLIENT_GET_PROPERTIES
                    .getString("apartment_not_available_page");

            public static final String CLIENT_HAS_APPLICATION_PAGE = CLIENT_GET_PROPERTIES
                    .getString("client_has_application_page");

            public static final String MONEY_VALUE_IS_INCORRECT = CLIENT_GET_PROPERTIES
                    .getString("money_value_is_incorrect_page");

            public static final String APPLICATION_CANCELED = CLIENT_GET_PROPERTIES
                    .getString("application_canceled_page");

            public static final String MAKE_TEMPORARY_APPLICATION_PAGE = CLIENT_GET_PROPERTIES
                    .getString("make_temporary_application_page");

            public static final String WRONG_STAY_LENGTH = CLIENT_GET_PROPERTIES
                    .getString("wrong_stay_length_page");

            public static final String APPLICATION_INVOICE_PAGE = CLIENT_GET_PROPERTIES
                    .getString("application_invoice_page");
        }

        public abstract static class Manager {
            public static final String PREFERRED_APARTMENTS_PAGE = MANAGER_GET_PROPERTIES
                    .getString("preferred_apartments_page");

            public static final String SHOW_PREFERRED_APARTMENTS = MANAGER_GET_PROPERTIES
                    .getString("show_preferred_apartments");

            public static final String CLIENT_HAS_ASSIGNED_APPLICATION_PAGE = MANAGER_GET_PROPERTIES
                    .getString("client_has_assigned_application_page");

            public static final String SUCCESSFUL_APPLICATION_ASSIGNMENT_PAGE = MANAGER_GET_PROPERTIES
                    .getString("successful_application_assignment_page");

            public static final String SHOW_TEMPORARY_APPLICATIONS = MANAGER_GET_PROPERTIES
                    .getString("show_temporary_applications");

            public static final String TEMPORARY_APPLICATIONS_PAGE = MANAGER_GET_PROPERTIES
                    .getString("temporary_applications_page");

            public static final String APARTMENTS_PAGE = MANAGER_GET_PROPERTIES
                    .getString("apartments_page");

            public static final String SHOW_APARTMENTS = MANAGER_GET_PROPERTIES
                    .getString("show_apartments");

            public static final String MANAGER_LIST_USERS_PAGE = MANAGER_GET_PROPERTIES
                    .getString("manager_list_users_page");

            public static final String MANAGER_LIST_USERS = MANAGER_GET_PROPERTIES
                    .getString("manager_list_users");
        }

        public abstract static class Admin {
            public static final String ADMIN_MANAGE_USERS = ADMIN_GET_PROPERTIES
                    .getString("admin_manage_users");

            public static final String SHOW_APARTMENTS_MANAGEMENT = ADMIN_GET_PROPERTIES
                    .getString("show_apartments_management");

            public static final String APARTMENT_MANAGEMENT_PAGE = ADMIN_GET_PROPERTIES
                    .getString("apartments_management_page");

            public static final String ADMIN_MANAGE_USERS_PAGE = ADMIN_GET_PROPERTIES
                    .getString("admin_manage_users_page");
        }

        public abstract static class User {
            public static final String SIGNUP_PAGE = USER_GET_PROPERTIES
                    .getString("signup_page");

            public static final String MAIN_PAGE = USER_GET_PROPERTIES
                    .getString("main_page");

            public static final String LOGIN = USER_GET_PROPERTIES
                    .getString("login");

            public static final String LOGIN_PAGE = USER_GET_PROPERTIES
                    .getString("login_page");

            public static final String LOGOUT = USER_GET_PROPERTIES
                    .getString("logout");

            public static final String MAIN = USER_GET_PROPERTIES
                    .getString("main");

            public static final String PROFILE = USER_GET_PROPERTIES
                    .getString("profile");

            public static final String PROFILE_PAGE = USER_GET_PROPERTIES
                    .getString("profile_page");
        }

        public static abstract class Error {
            public static final String ERROR_404 = ERROR_GET_PROPERTIES
                    .getString("error_404_page");

            public static final String ERROR_503 = ERROR_GET_PROPERTIES
                    .getString("error_503_page");
        }
    }

    public static abstract class Post {
        public static abstract class Client {
            public static final String CLIENT_APPLY = CLIENT_POST_PROPERTIES
                    .getString("client_apply");

            public static final String CONFIRM_PAYMENT = CLIENT_POST_PROPERTIES
                    .getString("confirm_payment");

            public static final String UPDATE_MONEY_ACCOUNT = CLIENT_POST_PROPERTIES
                    .getString("update_money_account");

            public static final String CANCEL_APPLICATION = CLIENT_POST_PROPERTIES
                    .getString("cancel_application");

            public static final String MAKE_TEMPORARY_APPLICATION = CLIENT_POST_PROPERTIES
                    .getString("make_temporary_application");
        }

        public static abstract class Manager {
            public static final String APPLY_FOR_CLIENT = MANAGER_POST_PROPERTIES
                    .getString("apply_for_client");
        }

        public static abstract class Admin {
            public static final String ADMIN_EDIT_USER = ADMIN_POST_PROPERTIES
                    .getString("admin_edit_user");

            public static final String EDIT_APARTMENT = ADMIN_POST_PROPERTIES
                    .getString("edit_apartment");
        }

        public static abstract class User {
            public static final String SIGNUP = USER_POST_PROPERTIES
                    .getString("signup");

            public static final String EDIT_PROFILE = USER_POST_PROPERTIES
                    .getString("edit_profile");
        }
    }
}
