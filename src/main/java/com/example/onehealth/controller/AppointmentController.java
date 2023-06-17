package com.example.onehealth.controller;

import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.event.AppointmentCancelledEvent;
import com.example.onehealth.service.AppointmentService;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
        modelMap.addAttribute("doctors", doctorList);
        modelMap.addAttribute("patients", patientList);
        return "addAppointment";
    }

    @PostMapping("/add")
    public String addAppointment(@ModelAttribute Appointment appointment) {
        LocalDateTime startDateTime = appointment.getStartTime().minusMinutes(30);
        LocalDateTime endDateTime = appointment.getStartTime().plusMinutes(30);
        appointment.setEndTime(endDateTime);
        appointmentService.addAppointment(appointment,startDateTime);
         return "redirect:/appointments";
    }

    @GetMapping("/remove")
    public String deleteAppointment(@RequestParam("id") int id) {
        appointmentService.delete(id);
        return "redirect:/appointments";
    }
    @Transactional
    @GetMapping("/cancell")
    public String rejectAppointment(@RequestParam("id") int id) {
        Optional<Appointment> byAppointmentId = appointmentService.getByAppointmentId(id);
        Appointment appointment = byAppointmentId.get();
        AppointmentCancelledEvent event = new AppointmentCancelledEvent(this,appointment.getPatient().getEmail());
        eventPublisher.publishEvent(event);
        appointmentService.delete(id);
        return "redirect:/doctor/appointments";
    }
}