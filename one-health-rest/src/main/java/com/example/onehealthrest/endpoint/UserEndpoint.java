package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.UserAuthRequestDto;
import com.example.onehealthcommon.dto.UserAuthResponseDto;
import com.example.onehealthcommon.dto.UserVerifyDto;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.mapper.UserMapper;
import com.example.onehealthrest.service.UserService;
import com.example.onehealthrest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserEndpoint {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil tokenUtil;
    private final UserMapper userMapper;

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthRequestDto userAuthRequestDto) {
        Optional<User> byEmail = userService.findByEmail(userAuthRequestDto.getEmail());
        if (byEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = byEmail.get();
        if (!passwordEncoder.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = tokenUtil.generateToken(userAuthRequestDto.getEmail());
        return ResponseEntity.ok(new UserAuthResponseDto(token));
    }

    @GetMapping("/verify-account")
    public ResponseEntity<UserVerifyDto> verifyUser(@RequestParam("email") String email,
                                                    @RequestParam("token") String token) {
        User user = userService.verifyAccount(email, token);
        if (user != null){
            return ResponseEntity.ok(userMapper.map(user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
