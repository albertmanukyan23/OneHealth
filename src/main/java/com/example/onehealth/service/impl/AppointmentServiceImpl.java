package com.example.onehealth.service.impl;
import com.example.onehealth.entity.Appointment;
import com.example.onehealth.repository.AppointmentRepository;
import com.example.onehealth.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAppointment() {
        return appointmentRepository.findAll();
    }

    @Override
    public void addAppointment(Appointment appointment,LocalDateTime start) {
        List<Appointment> byDateTime = appointmentRepository.
                findAllByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(start,appointment.getEndTime());
        if (byDateTime.isEmpty()) {
            appointmentRepository.save(appointment);
        }
    }

    @Override
    public void delete(int id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<Appointment> findByStartTimeAndEndTime(LocalDateTime startTime, LocalDateTime endTime) {
        return appointmentRepository.
                findAllByStartTimeGreaterThanEqualAndEndTimeLessThanEqual(startTime, endTime);
    }
}