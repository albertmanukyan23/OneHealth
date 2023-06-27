package com.example.onehealth.service;


import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.security.CurrentUser;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAppointment();

    void delete(int id);


    List<Appointment> getDoctorAppointments(int id);

    Optional<Appointment> getByAppointmentId(int id);


    List<Appointment> getPatientAppointments(int id);

    boolean isDoctorAvailableForAppointment(Appointment appointment);

    boolean createAppointment(Optional<Patient> patientById, Appointment appointment);


    void cancellAppointmentById(int id, CurrentUser currentUser);
}
