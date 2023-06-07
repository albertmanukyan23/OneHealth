package com.example.onehealth.controller;
import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private final UserService userService;
    private final DoctorService doctorService;

    @GetMapping
    public String getDoctors(ModelMap modelMap) {
        List<Doctor> doctors = doctorService.getDoctors();
        modelMap.addAttribute("doctors", doctors);
        return "doctors";
    }


    @GetMapping("/add")
    public String addDoctor() {
        return "addDoctor";
    }

    @PostMapping("/add")
    public String addDoctor(@ModelAttribute Doctor doctor) {
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        userService.registerUser(doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/remove")
    public String removeUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/doctor";
    }
}
