package com.example.onehealth.service;


import com.example.onehealth.entity.User;

import java.io.IOException;

public interface UserService {

    void registerUser(User user);

    void deleteUser(int id) throws IOException;


}
