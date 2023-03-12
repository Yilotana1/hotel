package com.example.hotel.model.dao.commons.mysql;

import static com.example.hotel.model.entity.enums.ApartmentStatus.FREE;
import static com.example.hotel.model.entity.enums.ApplicationStatus.CANCELED;

public interface ApartmentSQL {
    //SORTED IN ASCENDING ORDER
    String SELECT_APARTMENT_BY_NUMBER = "SELECT * FROM Apartment WHERE number = ?";
    String SELECT_APARTMENTS_SORTED_BY_CLASS = "SELECT number, floor, class_id, status_id, demand, price, number_of_people" +
            "  FROM Apartment JOIN Class ON class_id = class.id WHERE status_id = " + FREE.getId()
            + " ORDER BY Class.priority LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_STATUS = "SELECT * FROM Apartment ORDER BY status_id LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE = "SELECT * FROM Apartment" +
            " WHERE status_id = " + FREE.getId() + " ORDER BY number_of_people LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_PRICE = "SELECT * FROM Apartment WHERE status_id = " + FREE.getId() +
            " ORDER BY price LIMIT ?,?";
    String SELECT_APARTMENTS_BY_CLIENT_PREFERENCES = "SELECT * FROM Apartment WHERE" +
            " number_of_people = ? AND class_id = ? AND status_id = " + FREE.getId() + " ORDER BY Apartment.demand DESC LIMIT ?, ?";
    String SELECT_COUNT_APARTMENTS = "SELECT COUNT(number) AS count FROM Apartment";
    String SELECT_COUNT_PREFERRED_APARTMENTS = "SELECT COUNT(number)" +
            " AS count FROM Apartment JOIN Temporary_application" +
            " ON Apartment.class_id = Temporary_application.class_id" +
            " AND Apartment.number_of_people = Temporary_application.number_of_people" +
            " WHERE Temporary_application.client_login = ?";
    String SELECT_COUNT_APARTMENT_BY_CLASS_AND_NUMBER_OF_PEOPLE = "SELECT COUNT(number)" +
            "AS count FROM Apartment WHERE class_id = ? AND number_of_people = ?";
    String SELECT_APARTMENTS_AND_RESIDENTS_LOGINS = "SELECT Apartment.floor, Apartment.number, Apartment.class_id," +
            " Apartment.demand, Apartment.price, Apartment.status_id," +
            " Apartment.number_of_people,User.login" +
            " FROM Apartment LEFT JOIN Application ON  Apartment.number = Application.apartment_id" +
            " LEFT JOIN User ON Application.client_id = User.id LIMIT ?, ?";
    String SELECT_APARTMENT_BY_RESIDENT_LOGIN = "SELECT Apartment.floor,Apartment.number,Apartment.class_id," +
            "Apartment.demand,Apartment.price,Apartment.status_id,Apartment.number_of_people" +
            " FROM Apartment JOIN Application ON Application.apartment_id = Apartment.number JOIN User ON" +
            " Application.client_id = User.id WHERE Application.status_id != " + CANCELED.getId();
    //DML QUERIES
    String INSERT_APARTMENT = "INSERT INTO Apartment (number, floor, class_id, status_id, demand, price, number_of_people)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_APARTMENT = "UPDATE Apartment SET" +
            " floor = ?, class_id = ?, status_id = ?, demand = ?, price = ?, number_of_people = ? WHERE number = ?";
    String DELETE_APARTMENT_BY_NUMBER = "DELETE FROM Apartment WHERE number = ?";
}