package com.example.onehealth.service.impl;
import com.example.onehealth.entity.Appointment;
import com.example.onehealth.repository.AppointmentRepository;
import com.example.onehealth.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    @Override
    public List<Appointment> getDoctorAppointments(int id) {
        return appointmentRepository.findAllByDoctorId(id);
    }

    @Override
    public Optional<Appointment> getByAppointmentId(int id) {
        return appointmentRepository.findById(id);
    }
}
