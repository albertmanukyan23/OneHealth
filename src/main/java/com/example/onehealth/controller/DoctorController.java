package com.example.onehealth.controller;

import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.User;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final UserUtil userUtil;

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
    public String addDoctor(@ModelAttribute Doctor doctor,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        userUtil.saveProfilePicture(multipartFile, doctor);
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        userService.registerUser(doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/update")
    public String updateDoctor(@RequestParam("id") int id, ModelMap modelMap) {
        Optional<Doctor> doctorById = doctorService.findDoctorById(id);
        doctorById.ifPresent(doctor -> modelMap.addAttribute("doctor", doctor));
        return "updateDoctor";
    }

    @PostMapping("/update")
    public String updateDoctor(@ModelAttribute Doctor doctor,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {
        userUtil.saveProfilePicture(multipartFile, doctor);
        doctorService.update(doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/remove")
    public String removeUser(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/doctor";
    }
}
