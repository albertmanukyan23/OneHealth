package com.example.onehealth.service.impl;

import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.User;
import com.example.onehealth.repository.PatientRepository;
import com.example.onehealth.repository.UserRepository;
import com.example.onehealth.service.EmailSenderService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import lombok.RequiredArgsConstructor;
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
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;
    private final PatientRepository patientRepository;
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
            UUID token = UUID.randomUUID();
            user.setToken(token.toString());
            user.setEnabled(false);
            userRepository.save(user);
            verifyAccountWithEmail(user.getId());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(int id) throws IOException {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            imageDownloader.deleteProfilePicture(byId.get().getPicName());
        }
        userRepository.deleteById(id);
    }

    @Override
    public void passwordChange(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            if (byEmail.get().getToken().equals(token)) {
                User user = byEmail.get();
                user.setToken(null);
                userRepository.save(user);
            }
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

    @Override
    public void activateDeactivateUser(User user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if (byId.isPresent() && byId.get().isEnabled()) {
            User userDb = byId.get();
            userDb.setEnabled(false);
            userRepository.save(userDb);
            deactivateUser(user.getId());
        } else if (!byId.get().isEnabled()) {
            User userAct = byId.get();
            userAct.setEnabled(true);
            userRepository.save(userAct);
            activateUser(user.getId());
        }

    }


    public void verifyAccount(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            if (byEmail.get().getToken().equals(token)) {
                User user = byEmail.get();
                user.setEnabled(true);
                user.setToken(null);
                userRepository.save(user);
            }
        }
    }

    @Override
    public void confirmationMessage(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            UUID token = UUID.randomUUID();
            user.setToken(token.toString());
            userRepository.save(user);
            verifyAccountMessageEmail(user.getId());
        }

    }


    @Override
    public void updatePassword(String email, String token, String password, String passwordRepeat) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && byEmail.get().isEnabled()) {
            if (password.equals(passwordRepeat) && byEmail.get().getToken() == null) {
                User user = byEmail.get();
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }

        }
    }


    @Async
    public void verifyAccountMessageEmail(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "Confirm to rest password " +
                            siteUrl + "/user/password-change-page?email=" + user.getEmail() + "&token=" + user.getToken());
        }
    }

    @Async
    public void activateUser(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "You are activated"
            );
        }
    }

    @Async
    public void deactivateUser(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            emailSenderService.sendSimpleEmail(user.getEmail(),
                    "Welcome", "Hi" + user.getName() +
                            "\n" + "You are deactivated"
            );
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
                            siteUrl + "/user/verify-account?email=" + user.getEmail() + "&token=" + user.getToken());
        }
    }
}
