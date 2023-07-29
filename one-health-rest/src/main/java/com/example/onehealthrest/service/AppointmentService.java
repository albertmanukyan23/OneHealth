package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.AppointmentDto;
import com.example.onehealthcommon.dto.CreateAppointmentDto;
import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.entity.User;

import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<PatientAppointmentDto> getPatientAppointments(User user);

    List<AppointmentDto> getAppointmentDtos(int page, int size);

    Optional<AppointmentDto> createAppointment(User patientById, CreateAppointmentDto appointment);

    boolean cancelAppointmentById(int appointmentId, User currentUser);


}
