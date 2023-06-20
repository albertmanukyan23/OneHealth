package com.example.onehealth.controller;

import com.example.onehealth.entity.*;
import com.example.onehealth.event.AppointmentCancelledEvent;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentController {
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final DepartmentService departmentService;
    private final ApplicationEventPublisher eventPublisher;


    @GetMapping()
    public String appointmentPage(ModelMap modelMap) {
        List<Appointment> appointmentList = appointmentService.getAppointment();
        modelMap.addAttribute("appointments", appointmentList);
        return "appointments";
    }

    @GetMapping("/add")
    public String addAppointmentPage(ModelMap modelMap) {
        List<Doctor> doctorList = doctorService.getDoctors();
        List<Patient> patientList = patientService.getPatient();
        List<Department> department = departmentService.getDepartment();
        modelMap.addAttribute("doctors", doctorList);
        modelMap.addAttribute("departments", department);
        modelMap.addAttribute("patients", patientList);
        return "addAppointment";
    }

    @PostMapping("/add")
    public String addAppointment(@ModelAttribute Appointment appointment,
                                 @AuthenticationPrincipal CurrentUser currentUser) {
        if (appointment.getStartTime().getHour() >= 18) {
            return "redirect:/appointments/add";
        }
        Optional<Patient> patientById = patientService.findPatientById(currentUser.getUser().getId());
        if (patientById.isPresent()) {
            appointment.setPatient(patientById.get());
            LocalDateTime startDateTime = appointment.getStartTime().minusMinutes(30);
            LocalDateTime endDateTime = appointment.getStartTime().plusMinutes(30);
            appointment.setEndTime(endDateTime);
            appointmentService.addAppointment(appointment, startDateTime);
        }
        return "redirect:/patient/singlePage";
    }

    @GetMapping("/cancell")
    @Transactional
    public String cancelAppointment(@RequestParam("id") int id,
                                    @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Appointment> byAppointmentId = appointmentService.getByAppointmentId(id);
        if (byAppointmentId.isPresent()) {
            User user = currentUser.getUser();
            appointmentService.delete(id);
            if (user.getUserType() == UserType.DOCTOR) {
                Appointment appointment = byAppointmentId.get();
                AppointmentCancelledEvent event = new AppointmentCancelledEvent(this, appointment.getPatient().getEmail());
                eventPublisher.publishEvent(event);
                return "redirect:/doctor/appointments";
            } else if (user.getUserType() == UserType.PATIENT) {
                return "redirect:/patient/appointments";
            } else {
                return "redirect:/admin";
            }
        }
        return "redirect:/customLogin";
    }
}