package com.example.onehealth.controller;

import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.User;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.event.DoctorRegistrationEvent;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.AppointmentService;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
    private final ImageDownloader imageDownloader;
    private final AppointmentService appointmentService;
    private final ApplicationEventPublisher eventPublisher;


    @GetMapping
    public String getDoctors(ModelMap modelMap) {
        List<Doctor> doctors = doctorService.getDoctors();
        modelMap.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping("/singlePage")
    public String singlePage() {
        return "doctorSinglePage";
    }

    @GetMapping("/appointments")
    public String getDoctorPersonalAppointments(@AuthenticationPrincipal CurrentUser currentUser,
                                                ModelMap modelMap) {
        User user = currentUser.getUser();
        List<Appointment> doctorAppointments = appointmentService.getDoctorAppointments(user.getId());
        modelMap.addAttribute("appointments",doctorAppointments);
        return "doctorAppointments";
    }

    @GetMapping("/add")
    public String addDoctor(@ModelAttribute("doctor") Doctor doctor) {
        return "addDoctor";
    }

    @PostMapping("/add")
    @Transactional
    public String addDoctor(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                            @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "addDoctor";
        }
        doctor.setRegisDate(new Date());
        DoctorRegistrationEvent event = new DoctorRegistrationEvent(this,doctor.getEmail(),doctor.getPassword());
        eventPublisher.publishEvent(event);
        userService.registerUser(doctor);
        doctor.setUserType(UserType.DOCTOR);
        imageDownloader.saveProfilePicture(multipartFile, doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/update")
    public String updateDoctor(@ModelAttribute("doctor") Doctor doctor, ModelMap modelMap) {
        Optional<Doctor> doctorById = doctorService.findDoctorById(doctor.getId());
        doctorById.ifPresent(doctorFromDb -> modelMap.addAttribute("doctor", doctorFromDb));
        return "updateDoctor";
    }

    @PostMapping("/update")
    @Transactional
    public String updateDoctor(@ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "updateDoctor";
        }
        imageDownloader.saveProfilePicture(multipartFile, doctor);
        doctorService.update(doctor);
        return "redirect:/doctor";
    }

    @GetMapping("/remove")
    public String removeDoctor(@RequestParam("id") int id) {
        userService.deleteUser(id);
        return "redirect:/doctor";
    }

}
