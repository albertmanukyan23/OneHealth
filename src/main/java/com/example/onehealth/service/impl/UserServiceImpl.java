package com.example.onehealth.service.impl;

import com.example.onehealth.entity.User;
import com.example.onehealth.repository.UserRepository;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private  final ImageDownloader imageDownloader;


    private final UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        Optional<User> userFromDB = userRepository.findByEmail(user.getEmail());
        if (userFromDB.isEmpty()) {
            String password = user.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(int id) throws IOException {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()){
            imageDownloader.deleteProfilePicture(byId.get().getPicName());
        }
        userRepository.deleteById(id);
    }

}
