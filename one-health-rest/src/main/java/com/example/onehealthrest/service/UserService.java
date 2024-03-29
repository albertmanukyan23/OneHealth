package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.UserDto;
import com.example.onehealthcommon.dto.UserPasswordUpdaterDto;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthrest.security.CurrentUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    Optional<User> findByEmail(String email);

    void registerUser(User user);

    User verifyAccount(String email, String token);

    Optional<UserDto> uploadImageForUser(int id, MultipartFile multipartFile, CurrentUser currentUser);


    boolean updatePassword(UserPasswordUpdaterDto passwordUpdaterDto, User currentUser);

    boolean activateDeactivateUser(int id);
}
