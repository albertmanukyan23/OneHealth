package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.entity.Appointment;
import com.example.onehealthcommon.entity.User;

import java.util.List;

public interface AppointmentService {
    List<PatientAppointmentDto> getPatientAppointments(User user);
}
