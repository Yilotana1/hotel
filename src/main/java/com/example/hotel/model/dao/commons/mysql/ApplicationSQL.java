package com.example.hotel.model.dao.commons.mysql;


import static com.example.hotel.model.entity.enums.ApplicationStatus.APPROVED;
import static com.example.hotel.model.entity.enums.ApplicationStatus.CANCELED;
import static com.example.hotel.model.entity.enums.ApplicationStatus.NOT_APPROVED;

public interface ApplicationSQL {
    String SELECT_COUNT_APPLICATIONS = "SELECT COUNT(Application.id) AS count FROM Application";
    String SELECT_APPLICATION_BY_ID = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE Application.id = ?";
    String DELETE_APPLICATION_BY_ID = "DELETE FROM Application WHERE Application.id = ?";
    String INSERT_APPLICATION = "INSERT INTO Application" +
            "(client_id, apartment_id, status_id, price, creation_date, last_modified, start_date, end_date, stay_length)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_APPLICATION = "UPDATE Application SET" +
            " Application.client_id = ?, Application.apartment_id = ?," +
            " Application.status_id = ?, price = ?, last_modified = ?," +
            " start_date = ?, end_date = ?, stay_length = ? WHERE Application.id = ?";
    String SELECT_OUTDATED_APPLICATIONS = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE Application.status_id = " + APPROVED.getId() + " AND Application.end_date < curdate()";
    String SELECT_NOT_CANCELED_APPLICATION_BY_CLIENT_LOGIN = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE User.login = ? AND Application.status_id != " + CANCELED.getId();
    String SELECT_NOT_APPROVED_APPLICATION_BY_CLIENT_ID = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE Application.client_id = ? AND Application.status_id = ?";
    String SELECT_NOT_APPROVED_APPLICATION_BY_CLIENT_LOGIN = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE User.login = ? AND Application.status_id = " + NOT_APPROVED.getId();
    String SELECT_APPROVED_APPLICATION_BY_CLIENT_LOGIN = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE User.login = ? AND Application.status_id = " + APPROVED.getId();
    String SELECT_APPLICATIONS_BY_STATUS_ID = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE Application.status_id = ? LIMIT ?, ?";
    String SELECT_APPLICATIONS_BY_APARTMENT_ID = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " WHERE Application.apartment_id = ? LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_ID = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.id LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_CREATION_DATE = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.creation_date LIMIT ?, ?";

    String SELECT_APPLICATIONS_SORTED_BY_CREATION_DATE_DESC = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.creation_date DESC LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_LAST_MODIFIED_DATE = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.last_modified LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_LAST_MODIFIED_DATE_DESC = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.last_modified DESC LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_START_DATE = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.start_date LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_START_DATE_DESC = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.start_date DESC LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_END_DATE = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.end_date LIMIT ?, ?";
    String SELECT_APPLICATIONS_SORTED_BY_END_DATE_DESC = "SELECT * FROM Application" +
            " JOIN Application_status on Application.status_id = Application_status.id" +
            " JOIN User on Application.client_id = User.id" +
            " JOIN Apartment on Apartment.number = Application.apartment_id" +
            " ORDER BY Application.end_date DESC LIMIT ?, ?";
}
