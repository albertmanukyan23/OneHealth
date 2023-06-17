package com.example.onehealth.service;


import com.example.onehealth.entity.User;

public interface UserService {

    User registerUser(User user);
    void deleteUser(int id);

}
