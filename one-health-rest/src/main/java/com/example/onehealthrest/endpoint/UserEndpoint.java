package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.*;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.mapper.UserMapper;
import com.example.onehealthrest.security.CurrentUser;
import com.example.onehealthrest.service.UserService;
import com.example.onehealthrest.util.JwtTokenUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserEndpoint {

    @Value("${hospital.upload.image.path}")
    private String imageUploadPath;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil tokenUtil;
    private final UserMapper userMapper;

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthRequestDto userAuthRequestDto) {
        //todo remove method body to service
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
        log.info("verifyUser() method inside UserEndpoint has worked ");
        User user = userService.verifyAccount(email, token);
        return user != null ? ResponseEntity.ok(userMapper.map(user)) : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<UserDto> uploadImage(@PathVariable("id") int id,
                                               @RequestParam("image") MultipartFile multipartFile,
                                               @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        log.info("uploadImage() method inside UserEndpoint has worked ");
        Optional<UserDto> userDtoOptional = userService.uploadImageForUser(id, multipartFile, currentUser);
        return userDtoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/getImage",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        File file = new File(imageUploadPath + picName);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                return IOUtils.toByteArray(fis);
            }
        }
        return null;
    }

    @GetMapping("/single-page")
    public ResponseEntity<UserPageDto> singlePage(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(userMapper.mapToUserPageDto(currentUser.getUser()));
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UserPasswordUpdaterDto passwordUpdaterDto,
                                            @AuthenticationPrincipal CurrentUser currentUser, BindingResult bindingResult) {
        StringBuilder stringBuilder = userService.checkValidation(bindingResult);
        if (!stringBuilder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringBuilder.toString());
        }
        log.info("changePasswordPage() method inside UserEndpoint has worked ");
        return userService.updatePassword(passwordUpdaterDto, currentUser.getUser()) ? ResponseEntity.status(HttpStatus.ACCEPTED).build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/activate-deactivate/{id}")
    public ResponseEntity<?> activateDeactivate(@PathVariable("id") int id) {
        log.info("activateDeactivate() method inside UserEndpoint has worked ");
        return userService.activateDeactivateUser(id) ? ResponseEntity.status(HttpStatus.ACCEPTED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

}
