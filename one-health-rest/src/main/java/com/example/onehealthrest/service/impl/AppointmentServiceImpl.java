package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.entity.Appointment;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.mapper.AppointmentMapper;
import com.example.onehealthcommon.repository.AppointmentRepository;
import com.example.onehealthrest.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private  final AppointmentMapper appointmentMapper;

    @Override
    public List<PatientAppointmentDto> getPatientAppointments(User user) {

        return appointmentMapper.map(appointmentRepository.findAllByPatientId(user.getId()));
    }
}
