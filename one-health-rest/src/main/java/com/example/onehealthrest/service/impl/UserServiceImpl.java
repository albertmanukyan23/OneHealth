package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.EmailSenderService;
import com.example.onehealthcommon.dto.UserDto;
import com.example.onehealthcommon.dto.UserPasswordUpdaterDto;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.mapper.UserMapper;
import com.example.onehealthcommon.repository.UserRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthrest.exception.ImageProcessingException;
import com.example.onehealthrest.security.CurrentUser;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${site.url}")
    private String siteUrl;

    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void registerUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isEmpty()) {
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            UUID token = UUID.randomUUID();
            user.setToken(token.toString());
            user.setEnabled(false);
            userRepository.save(user);
            log.info("User has successfully registered  ");
            verifyAccountWithEmail(user.getId());
        }
    }

    @Async
    public void verifyAccountWithEmail(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "Please verify your email by clicking on this url " +
                            siteUrl + "/users/verify-account?email=" + user.getEmail() + "&token=" + user.getToken());
            log.info("User's verification message has been send to the user with the  " + id + " id");
        }
    }

    public User verifyAccount(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && (byEmail.get().getToken().equals(token)) && !byEmail.get().isEnabled()) {
            User user = byEmail.get();
            user.setEnabled(true);
            user.setToken(null);
            log.info("User  with the  " + user.getId() + " id has verified his account");
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    @Transactional
    public Optional<UserDto> uploadImageForUser(int id, MultipartFile multipartFile, CurrentUser currentUser)  {
        try {
            if (currentUser.getUser().getId() == id) {
                Optional<User> userOptional = userRepository.findById(id);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    imageDownloader.saveProfilePicture(multipartFile, userOptional.get());
                    userRepository.save(user);
                    UserDto userDto = userMapper.mapToDto(user);
                    log.info("uploadImageForUser() in UserServiceImpl has successfully worked");
                    return Optional.of(userDto);
                }
            }
            return Optional.empty();

        } catch (IOException e) {
            throw new ImageProcessingException("Image uploading failed");
        }

    }

    @Override
    public StringBuilder checkValidation(BindingResult bindingResult) {
        StringBuilder errorBuilder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> errorBuilder.append(error.getDefaultMessage()).append("\n"));
            log.info("checkValidation() in UserServiceImpl found errors");
        }
        return errorBuilder;
    }


    @Override
    @Transactional
    public boolean updatePassword(UserPasswordUpdaterDto dto, User currentUser) {
        boolean isPasswordUpdated = false;
        Optional<User> byEmail = userRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent() && currentUser.getId() == byEmail.get().getId()) {
            User user = byEmail.get();
            if (passwordCanBeUpdated(dto, user)) {
                user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
                isPasswordUpdated = true;
                userRepository.save(user);
                log.info("updatePassword() in UserServiceImpl has successfully worked");

            }
        }
        return isPasswordUpdated;
    }

    private boolean passwordCanBeUpdated(UserPasswordUpdaterDto dto, User user) {
        return user.isEnabled() &&
                passwordEncoder.matches(dto.getOldPassword(), user.getPassword()) &&
                user.getToken() == null;
    }

    @Override
    @Transactional
    public boolean activateDeactivateUser(int id) {
        boolean isProcessDone = false;
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            updateUserStatus(user);
            userRepository.save(user);
            isProcessDone = true;
            log.info("activateDeactivateUser() in UserServiceImpl has successfully worked");
        }
        return isProcessDone;
    }

    private void updateUserStatus(User user) {
        if (user.isEnabled()) {
            user.setEnabled(false);
            sendBlockMessage(user);
        } else {
            user.setEnabled(true);
            user.setToken(null);
            sendActivationMessage(user);
        }
    }

    private void sendActivationMessage(User user) {
        emailSenderService.sendSimpleEmail(user.getEmail(),
                "You are unblocked", "Hi" + user.getName() +
                        "\n" + "You are active again");
        log.info("Activation message  was send to the user with " + user.getId() + " id");
    }


    private void sendBlockMessage(User user) {
        emailSenderService.sendSimpleEmail(user.getEmail(),
                "You are blocked ", "Hi" + user.getName() +
                        "\n" + "You are deactivated by Admin");
        log.info("Deactivation message  was send to the user with " + user.getId() + " id");
    }
}



