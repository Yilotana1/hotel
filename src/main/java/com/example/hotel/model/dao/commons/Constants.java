package com.example.hotel.model.dao.commons;

public abstract class Constants {
    public static abstract class ColumnLabels {
        public static final String COUNT = "count";

        public static abstract class Apartment {
            public static final String NUMBER = "apartment.number";
            public static final String FLOOR = "apartment.floor";
            public static final String DEMAND = "apartment.demand";
            public static final String PRICE = "apartment.price";
            public static final String CLASS_ID = "apartment.class_id";
            public static final String STATUS_ID = "apartment.status_id";
            public static final String NUMBER_OF_PEOPLE = "apartment.number_of_people";
        }

        public static abstract class Application {
            public static final String ID = "application.id";
            public static final String START_DATE = "application.start_date";
            public static final String END_DATE = "application.end_date";
            public static final String STATUS_ID = "application.status_id";
            public static final String PRICE = "application.price";
            public static final String CREATION_DATE = "application.creation_date";
            public static final String LAST_MODIFIED = "application.last_modified";
            public static final String STAY_LENGTH = "application.stay_length";
        }

        public static abstract class Role {
            public static final String ROLE = "role.role";
        }

        public static abstract class TemporaryApplication {
            public static final String ID = "temporary_application.id";
            public static final String CLASS_ID = "temporary_application.class_id";
            public static final String NUMBER_OF_PEOPLE = "temporary_application.number_of_people";
            public static final String STAY_LENGTH = "temporary_application.stay_length";
            public static final String CLIENT_LOGIN = "temporary_application.client_login";
            public static final String CREATION_DATE = "temporary_application.creation_date";
        }

        public static abstract class User {
            public static final String ID = "user.id";
            public static final String LOGIN = "user.login";
            public static final String FIRSTNAME = "user.firstname";
            public static final String LASTNAME = "user.lastname";
            public static final String PASSWORD = "user.password";
            public static final String EMAIL = "user.email";
            public static final String PHONE = "user.phone";
            public static final String STATUS_ID = "user.status_id";
            public static final String MONEY = "user.money";
        }
    }
}
