package com.example.hotel.model.dao.sql.mysql;

import static com.example.hotel.model.entity.enums.ApartmentStatus.FREE;

public interface ApartmentSQL {
    //SORTED IN ASCENDING ORDER
    String SELECT_APARTMENT_BY_NUMBER = "SELECT * FROM Apartment WHERE number = ?";
    String SELECT_APARTMENTS_SORTED_BY_NUMBER = "SELECT * FROM Apartment WHERE status_id = " + FREE.getId() + " ORDER BY number LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_CLASS = "SELECT number, floor, class_id, status_id, demand, price, number_of_people" +
            "  FROM Apartment JOIN Class ON class_id = class.id WHERE status_id = " + FREE.getId()
            + " ORDER BY Class.priority LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_STATUS = "SELECT * FROM Apartment ORDER BY status_id LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE = "SELECT * FROM Apartment" +
            " WHERE status_id = " + FREE.getId() + " ORDER BY number_of_people LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_PRICE = "SELECT * FROM Apartment WHERE status_id = " + FREE.getId() +
            " ORDER BY price LIMIT ?,?";
    String SELECT_APARTMENTS_BY_CLASS = "SELECT * FROM Apartment WHERE class_id = ?" +
            " AND status_id = " + FREE.getId() + " LIMIT ?, ?";
    //IN DESCENDING ORDER
    String SELECT_APARTMENTS_SORTED_BY_CLASS_DESC = "SELECT number, floor, class_id, status_id, demand, price, number_of_people" +
            "  FROM Apartment JOIN Class ON class_id = class.id ORDER BY Class.priority DESC LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_NUMBER_DESC = "SELECT * FROM Apartment ORDER BY number DESC LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_NUMBER_OF_PEOPLE_DESC = "SELECT * FROM Apartment ORDER BY number_of_people DESC LIMIT ?,?";
    String SELECT_APARTMENTS_SORTED_BY_PRICE_DESC = "SELECT * FROM Apartment ORDER BY price DESC LIMIT ?,?";
    // SELECTED BY CRITERIA
    String SELECT_APARTMENTS_BY_STATUS = "SELECT * FROM Apartment WHERE status_id = ? LIMIT ?, ?";
    String SELECT_APARTMENTS_BY_NUMBER_OF_PEOPLE = "SELECT * FROM Apartment WHERE number_of_people = ? LIMIT ?, ?";
    String SELECT_APARTMENTS_BY_FLOOR = "SELECT * FROM Apartment WHERE floor = ? LIMIT ?, ?";
    String SELECT_COUNT_APARTMENTS = "SELECT COUNT(number) AS count FROM Apartment";
    //DML QUERIES
    String INSERT_APARTMENT = "INSERT INTO Apartment (number, floor, class_id, status_id, demand, price, number_of_people)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_APARTMENT = "UPDATE Apartment SET" +
            " floor = ?, class_id = ?, status_id = ?, demand = ?, price = ?, number_of_people = ? WHERE number = ?";
    String DELETE_APARTMENT_BY_NUMBER = "DELETE FROM Apartment WHERE number = ?";
}