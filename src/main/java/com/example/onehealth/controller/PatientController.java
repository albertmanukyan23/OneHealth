package com.example.onehealth.controller;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.service.PatientService;
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
@RequestMapping("/patient")
public class PatientController {
    private final UserService userService;
    private final PatientService patientService;
    private final UserUtil userUtil;
    @GetMapping()
    public String getPatient(ModelMap modelMap) {
        List<Patient> patientList = patientService.getPatient();
        modelMap.addAttribute("patients", patientList);
        return "patients";
    }
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Patient patient,
                           @RequestParam("image") MultipartFile multipartFile,
                           UserUtil userUtil) throws IOException {
        patient.setRegisDate(new Date());
        userUtil.saveProfilePicture(multipartFile, patient);
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
        return "redirect:/";
    }

    @GetMapping("/update")
    public String updatePatient(ModelMap modelMap,
                                @RequestParam("id") int id) {
        Optional<Patient> patientById = patientService.findPatientById(id);
        patientById.ifPresent(patient -> modelMap.addAttribute("patient", patient));
        return "updatePatient";
    }
    @PostMapping("/update")
    public String updatePatient(@ModelAttribute Patient patient,
                                @RequestParam("image") MultipartFile multipartFile) throws IOException {
        patient.setRegisDate(new Date());
        userUtil.saveProfilePicture(multipartFile, patient);
        patientService.update(patient);
        return "redirect:/patient";
    }
    @GetMapping("/delete")
    public String removeUser(@RequestParam("id") int id) throws IOException {
        userService.deleteUser(id);
        return "redirect:/patient";
    }
}