package com.example.onehealth.controller;

import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.User;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {
    private final UserService userService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Patient patient) {
        patient.setRegisDate(new Date());
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
        return "redirect:/";
    }
}
