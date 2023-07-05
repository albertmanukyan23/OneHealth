package com.example.onehealth.controller;

import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.User;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.AppointmentService;
import com.example.onehealth.service.PatientService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Controller
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final UserService userService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    @GetMapping()
    public String getPatient(ModelMap modelMap,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size
    ) {
        Page<Patient> result = patientService.getPatientPageData(page, size);
        List<Integer> pageNumbers = patientService.getPageNumbers(result.getTotalPages());
        modelMap.addAttribute("pageNumbers", pageNumbers);
        modelMap.addAttribute("patients", result);
        return "patients";
    }

    @GetMapping("/singlePage")
    public String singlePage(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap) {
        modelMap.addAttribute("patient", currentUser.getUser());
        return "patientSinglePage";
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("patient") Patient patient) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("patient") @Valid Patient patient, BindingResult bindingResult,
                           @RequestParam("image") MultipartFile multipartFile,
                           ImageDownloader userUtil) throws IOException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        patientService.save(multipartFile, patient);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String updatePatient(ModelMap modelMap,
                                @ModelAttribute("patient") Patient patient) {
        Optional<Patient> patientById = patientService.findPatientById(patient.getId());
        patientById.ifPresent(patientFromDb -> modelMap.addAttribute("patient", patientFromDb));
        return "updatePatient";
    }

    @GetMapping("/appointments")
    public String getDoctorPersonalAppointments(@AuthenticationPrincipal CurrentUser currentUser,
                                                ModelMap modelMap) {
        User user = currentUser.getUser();
        List<Appointment> patientAppointments = appointmentService.getPatientAppointments(user.getId());
        modelMap.addAttribute("appointments", patientAppointments);
        return "patientAppointments";
    }

    @PostMapping("/update")
    public String updatePatient(@ModelAttribute("patient") @Valid Patient patient, BindingResult bindingResult,
                                @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (bindingResult.hasErrors()) {
            return "updatePatient";
        }
        patientService.update(patient, multipartFile);
        return "redirect:/patient";
    }

    @GetMapping("/delete")
    public String removeUser(@RequestParam("id") int id) throws IOException {
        userService.deleteUser(id);
        return "redirect:/patient";
    }
}
