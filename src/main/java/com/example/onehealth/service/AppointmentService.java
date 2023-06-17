package com.example.onehealth.service;


import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAppointment();

    void addAppointment(Appointment appointment,LocalDateTime start);

    void delete(int id);

    List<Appointment> findByStartTimeAndEndTime(LocalDateTime startTime,LocalDateTime endTime);

    List<Appointment> getDoctorAppointments(int id);
}
