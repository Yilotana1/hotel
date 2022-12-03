package com.example.hotel.model.service;

import com.example.hotel.controller.dto.UserDTO;
import com.example.hotel.model.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> signIn(String login, String password);
    Optional<User> getByLogin(String login);
    User signUp(UserDTO userDTO);
    void update(UserDTO userDTO);
    void block(String userLogin);
    void changeToManager(String userLogin);
    void changeToClient(String userLogin);
}
