package com.example.onehealth.service;

import com.example.onehealth.entity.User;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    void registerUser(User user);

    void deleteUser(int id) throws IOException;

    void verifyAccount(String email, String token);

    void confirmationMessage(String email);

    void updatePassword(String email, String token, String password, String passwordRepeat);

    Optional<User> findByEmail(String email);

    void passwordChangePage(String email, String token);

}
