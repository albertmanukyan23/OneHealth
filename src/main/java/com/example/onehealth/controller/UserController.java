package com.example.onehealth.controller;

import com.example.onehealth.entity.User;
import com.example.onehealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/verify-account")
    public String verifyUser(@RequestParam("email") String email,
                             @RequestParam("token") String token) {
        Optional<User> byEmail = userService.findByEmail(email);
        if (byEmail.isEmpty()) {
            return "redirect:/";
        }
        if (byEmail.get().isEnabled()) {
            return "redirect:/";
        }
        userService.verifyAccount(email, token);
        return "redirect:/customLogin";
    }

    @GetMapping("/email-confirmation-page")
    public String confirmationPage() {
        return "confirmEmail";
    }

    @GetMapping("/confirm-email")
    public String confirmationEmail(@RequestParam("email") String email) {
        userService.confirmationMessage(email);
        return "redirect:/customLogin";
    }
    @GetMapping("/password-change-page")
    public String changePasswordPage(ModelMap modelMap, @RequestParam("token") String token,
                                     @RequestParam("email") String email) {
        userService.passwordChangePage(email, token);
        modelMap.addAttribute("email", email);
        return "passwordChangePage";
    }

//    @PostMapping("/changePassword")
//    public String changePasswordPage(@RequestParam("password") String password,
//                                     @RequestParam("passwordRepeat") String passwordRepeat,
//                                     @RequestParam("email") String email,
//                                     @RequestParam("token") String token
//                                     )
//    {
//       patientService.updatePassword(email,token,password,passwordRepeat);
//        return "redirect:/customLogin";
//    }
}
