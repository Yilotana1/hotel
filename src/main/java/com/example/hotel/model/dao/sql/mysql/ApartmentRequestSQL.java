package com.example.hotel.model.dao.sql.mysql;

public interface ApartmentRequestSQL {
    String SELECT_COUNT_TEMPORARY_APPLICATION = "SELECT COUNT(id) AS count FROM Temporary_application";
    String INSERT_TEMPORARY_APPLICATION = "INSERT INTO Temporary_application" +
            " (class_id, number_of_people, stay_length, client_login)" +
            " VALUES (?, ?, ?, ?)";
    String UPDATE_APARTMENT_REQUEST = "UPDATE Temporary_application SET" +
            " class_id = ?, number_of_people = ?, stay_length WHERE id = ?";
    String DELETE_TEMPORARY_APPLICATION_BY_ID = "DELETE FROM Temporary_application WHERE id = ?";
    String DELETE_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN = "DELETE FROM Temporary_application" +
            " WHERE Temporary_application.client_login = ?";
    String SELECT_APARTMENT_REQUEST_BY_ID = "SELECT * FROM Temporary_application WHERE id = ?";
    String SELECT_TEMPORARY_APPLICATION_BY_CLIENT_LOGIN = "SELECT * FROM Temporary_application WHERE client_login = ?";
    String SELECT_APARTMENT_REQUESTS_SORTED_BY_NUMBER = "SELECT * FROM Temporary_application ORDER BY id LIMIT ?,?";
}
