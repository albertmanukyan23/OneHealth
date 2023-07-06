package com.example.onehealth.controller;

import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.AppointmentService;
import com.example.onehealth.service.DepartmentService;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;

    @GetMapping
    public String appointmentPage(ModelMap modelMap) {
        List<Appointment> appointmentList = appointmentService.getAppointment();
        modelMap.addAttribute("appointments", appointmentList);
        return "appointments";
    }

    @GetMapping("/make")
    //todo to-make
    public String addAppointmentPage(ModelMap modelMap) {
        modelMap.addAttribute("doctors", doctorService.getDoctors());
        modelMap.addAttribute("departments", departmentService.getDepartmentList());
        return "addAppointment";
    }

    @PostMapping("/make")
    public String addAppointment(@ModelAttribute Appointment appointment,
                                 @AuthenticationPrincipal CurrentUser currentUser) {
        if (appointmentService.createAppointment(patientService.findPatientById(currentUser.getUser().getId()), appointment)) {
            return "redirect:/patient/appointments";
        }
        return "redirect:/appointments/failed";
    }

    @GetMapping("/failed")
    public String addAppointment() {
        return "appointmentFailedView";
    }

    @GetMapping("/cancell")
    //todo cancel
    public String cancelAppointment(@RequestParam("id") int id,
                                    @AuthenticationPrincipal CurrentUser currentUser) {
        appointmentService.cancellAppointmentById(id, currentUser);
        if (currentUser.getUser().getUserType() == UserType.DOCTOR) {
            return "redirect:/doctor/appointments";
        } else if (currentUser.getUser().getUserType() == UserType.PATIENT) {
            return "redirect:/patient/appointments";
        } else {
            return "redirect:/admin";
        }
    }
}