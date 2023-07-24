package com.example.onehealthrest.service.impl;
import com.example.onehealthcommon.EmailSenderService;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.repository.UserRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${site.url}")
    private String siteUrl;

    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;
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
                            siteUrl + "/user/verify-account?email=" + user.getEmail() + "&token=" + user.getToken());
        }
    }
    public User verifyAccount(String email, String token) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent() && (byEmail.get().getToken().equals(token)) && !byEmail.get().isEnabled()) {
                User user = byEmail.get();
                user.setEnabled(true);
                user.setToken(null);
                return userRepository.save(user);
        }
        return null;
    }
}
