package com.example.hotel.controller.commons;

import static java.util.ResourceBundle.getBundle;

public abstract class Constants {

    public static final String BASE_URL = getBundle("application").getString("base_url");

    public abstract static class SessionAttributes {
        public static final String LOGIN = "login";
        public static final String STATUS = "status";
        public static final String ROLES = "roles";
        public static final String INVALID_SUFFIX = "_is_invalid";
    }

    public abstract static class ServletContextAttributes {
        public static final String LOGGED_USERS = "loggedUsers";
    }

    public abstract static class RequestAttributes {
        public static final String ERROR_PREFIX = "error";
        public static final String APARTMENTS = "apartments";
        public static final String USERS = "users";
        public static final String APPLICATION = "application";
        public static final String LOGIN = "login";
        public static final String STAY_LENGTH = "stay_length";
        public static final String TEMPORARY_APPLICATIONS = "temporary_applications";
        public static final String USER = "user";

        public abstract static class PaginationAttributes {
            public static final String TOTAL_PAGES_NUMBER = "count";
            public static final String PAGE = "page";
            public static final String DEFAULT_PAGE = "1";
            public static final String LIST_OF_SORTING_OPTIONS = "sorted_by_list";
            public static final String SORTED_BY = "sorted_by";
            public static final String DEFAULT_SORTING = "default_sorting";
        }
    }

    public abstract static class RequestParameters {
        public static final String LOGIN = "login";
        public static final String BUSY_STATUS = "busy";
        public static final String BLOCKED_STATUS = "blocked";
        public static final String ID = "id";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String CLIENT_MONEY = "money";
        public static final String STAY_LENGTH = "stay_length";
        public static final String APARTMENT_NUMBER = "number";
        public static final String APARTMENT_CLASS_ID = "apartment_class_id";
        public static final String NUMBER_OF_PEOPLE = "number_of_people";
        public static final String BY_LOGIN = "by_login";
        public static final String FIRSTNAME = "firstname";
        public static final String LASTNAME = "lastname";
        public static final String PHONE = "phone";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String SORTED_BY = "sorted_by";
        public static final String PRICE = "price";
    }
}
