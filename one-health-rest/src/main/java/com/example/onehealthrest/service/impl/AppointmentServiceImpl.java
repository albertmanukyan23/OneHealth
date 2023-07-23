package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.repository.AppointmentRepository;
import com.example.onehealthrest.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

}
