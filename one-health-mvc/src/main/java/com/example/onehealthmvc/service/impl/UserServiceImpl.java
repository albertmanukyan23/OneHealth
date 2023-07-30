package com.example.onehealthmvc.service.impl;

import com.example.onehealthcommon.component.EmailSenderService;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.exception.ImageProcessingException;
import com.example.onehealthcommon.repository.UserRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthmvc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;


    @Value("${site.url}")
    private String siteUrl;

    @Override
    public void registerUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isEmpty()) {
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            if (user.getUserType() == UserType.PATIENT) {
                UUID token = UUID.randomUUID();
                user.setToken(token.toString());
                user.setEnabled(false);
            }
            userRepository.save(user);
            log.info("registerUser() method has been worked successfully");
            verifyAccountWithEmail(user.getId());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(int id) {
        try {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                imageDownloader.deleteProfilePicture(byId.get().getPicName());
            }
            userRepository.deleteById(id);
        } catch (IOException e) {
            log.info("Catch ImageProcessingException() exception during the deleting  user with id " + id);
            throw new ImageProcessingException("Image uploading failed");
        }
    }

    @Override
    public void passwordChange(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && (byEmail.get().getToken().equals(token))) {
            User user = byEmail.get();
            user.setToken(null);
            userRepository.save(user);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //Activates or deactivates the user account based on the current enabled status
    @Override
    public void activateDeactivateUser(User user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if (byId.isPresent()) {
            User userDb = byId.get();
            if (user.isEnabled()) {
                userDb.setEnabled(false);
                userRepository.save(userDb);
                sendUserDeactivationMessage(user.getId());
            } else {
                userDb.setEnabled(true);
                userRepository.save(userDb);
                sendUserActivationMessage(user.getId());
            }
        }
    }


    public void verifyAccount(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && (byEmail.get().getToken().equals(token))) {
            User user = byEmail.get();
            user.setEnabled(true);
            user.setToken(null);
            userRepository.save(user);
        }
    }

    //Sends a confirmation message with a unique token to the user's email address
    @Override
    public void confirmationMessage(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            UUID token = UUID.randomUUID();
            user.setToken(token.toString());
            userRepository.save(user);
            sendEmailVerificationMessage(user.getId());
        }

    }


    @Override
    public void updatePassword(String email, String token, String password, String passwordRepeat) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && byEmail.get().isEnabled() &&
                (password.equals(passwordRepeat) && byEmail.get().getToken() == null)) {
            User user = byEmail.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }


    @Async
    public void sendEmailVerificationMessage(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "Confirm to rest password " +
                            siteUrl + "/user/password-change-page?email=" + user.getEmail() + "&token=" + user.getToken());
        }
    }

    public void sendUserActivationMessage(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "You are activated"
            );
        }
    }

    public void sendUserDeactivationMessage(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "You are deactivated"
            );
        }
    }

    public void verifyAccountWithEmail(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "Please verify your email by clicking on this url " +
                            siteUrl + "/user/verify-account?email=" + user.getEmail() + "&token=" + user.getToken());
        }
    }
}
