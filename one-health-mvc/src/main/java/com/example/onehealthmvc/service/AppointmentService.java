package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.Appointment;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthmvc.security.CurrentUser;


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


    void cancelAppointmentById(int id, CurrentUser currentUser);

    void applicationByLetterToDoDoctorZoomData(Patient patient, Doctor doctor);

    List<Patient> findDoctorPatientsFromAppointments(int id, String searchText);
}
