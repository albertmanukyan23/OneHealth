package com.example.onehealthmvc.controller;
import com.example.onehealthcommon.entity.Appointment;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthmvc.security.CurrentUser;
import com.example.onehealthmvc.service.AppointmentService;
import com.example.onehealthmvc.service.DepartmentService;
import com.example.onehealthmvc.service.DoctorService;
import com.example.onehealthmvc.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
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
            return "redirect:/patients/appointments";
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
        appointmentService.cancelAppointmentById(id, currentUser);
        if (currentUser.getUser().getUserType() == UserType.DOCTOR) {
            return "redirect:/doctor/appointments";
        } else if (currentUser.getUser().getUserType() == UserType.PATIENT) {
            return "redirect:/patients/appointments";
        } else {
            return "redirect:/admin";
        }
    }
}