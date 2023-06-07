package com.example.onehealth.service;


import com.example.onehealth.entity.User;

public interface UserService {
    void registerUser(User user);
    void deleteUser(int id);
}
