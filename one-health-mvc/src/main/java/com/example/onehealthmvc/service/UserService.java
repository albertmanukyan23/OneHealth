package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    void registerUser(User user);

    void deleteUser(int id) throws IOException;

    void verifyAccount(String email, String token);

    void confirmationMessage(String email);

    void updatePassword(String email, String token, String password, String passwordRepeat);

    Optional<User> findByEmail(String email);

    void passwordChange(String email, String token);

    Optional<User> findById(int id);

    List<User> findAll();

    void activateDeactivateUser(User user);

}
