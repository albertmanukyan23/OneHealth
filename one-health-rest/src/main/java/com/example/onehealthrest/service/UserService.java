package com.example.onehealthrest.service;

import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.User;
import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    void registerUser(User user);

    void verifyAccount(String email, String token);
}
