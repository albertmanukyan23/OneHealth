package com.example.onehealth.service.impl;

import com.example.onehealth.entity.*;
import com.example.onehealth.repository.AppointmentRepository;
import com.example.onehealth.repository.DoctorRepository;
import com.example.onehealth.repository.PatientRepository;
import com.example.onehealth.security.CurrentUser;
import com.example.onehealth.service.AppointmentService;
import com.example.onehealth.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmailSenderService emailSenderService;
    private final DoctorRepository doctorRepository;

    @Override
    public List<Appointment> getAppointment() {
        return appointmentRepository.findAll();
    }


    @Override
    public void delete(int id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getDoctorAppointments(int id) {
        return appointmentRepository.findAllByDoctorId(id);
    }

    @Override
    public Optional<Appointment> getByAppointmentId(int id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> getPatientAppointments(int id) {
        return appointmentRepository.findAllByPatientId(id);
    }

    @Override
    public boolean isDoctorAvailableForAppointment(Appointment appointment) {
        boolean isDoctorAvailable = true;
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorId(appointment.getDoctor().getId());
        LocalDateTime newAppointmentStartDate = appointment.getStartTime();
        for (Appointment existingAppointment : doctorAppointments) {
            LocalDateTime existedAppointmentStartDate = existingAppointment.getStartTime().minusMinutes(30);
            if (newAppointmentStartDate.isAfter(existedAppointmentStartDate) &&
                    newAppointmentStartDate.isBefore(existingAppointment.getEndTime())) {
                isDoctorAvailable = false;
                break;
            }
        }
        return isDoctorAvailable;
    }

    private boolean isAppointmentBetweenWorkingHours(Appointment appointment) {
        return appointment.getStartTime().getHour() >= 8 && appointment.getStartTime().getHour() < 18;
    }

    @Override
    @Transactional
    public boolean createAppointment(Optional<Patient> patientById, Appointment appointment) {
        //   TODO handle null pointer exception;
        boolean isAppointmentCreated = false;
        if (patientById.isPresent() && isDoctorAvailableForAppointment(appointment)
                && isAppointmentBetweenWorkingHours(appointment)) {
            if (appointment.getRegisterType() == RegisterType.ONLINE) {
                Optional<Doctor> doctorOptional = doctorRepository.findById(appointment.getDoctor().getId());
                Optional<Patient> patientOptional = patientRepository.findById(patientById.get().getId());
                if (patientOptional.isPresent() && doctorOptional.isPresent()) {
                    applicationByLetterToDoDoctorZoomData(patientOptional.get(), doctorOptional.get());
                }
            }
            appointment.setPatient(patientById.get());
            LocalDateTime appointmentEndTime = appointment.getStartTime().plusMinutes(30);
            appointment.setEndTime(appointmentEndTime);
            appointmentRepository.save(appointment);
            isAppointmentCreated = true;
        }
        return isAppointmentCreated;
    }

    @Override
    @Transactional
    public void cancelAppointmentById(int id, CurrentUser currentUser) {
        Optional<Appointment> byAppointmentId = appointmentRepository.findById(id);
        if (byAppointmentId.isPresent()) {
            User user = currentUser.getUser();
            appointmentRepository.deleteById(id);
            if (user.getUserType() == UserType.DOCTOR) {
                Appointment appointment = byAppointmentId.get();
                sendAppointmentCancelMessageToPatient(appointment.getPatient().getId());
            }
        }
    }


    public void sendAppointmentCancelMessageToPatient(int id) {
        Optional<Patient> patientFromDb = patientRepository.findById(id);
        if (patientFromDb.isPresent()) {
            Patient patient = patientFromDb.get();
            emailSenderService.sendSimpleEmail(patient.getEmail(), "Appointment was cancelled",
                    "Your appointment has been canceled by the doctor," +
                            " we ask for your forgiveness, try to book a consultation again for another day.");
        }
    }


    public void applicationByLetterToDoDoctorZoomData(Patient patient, Doctor doctor) {
        emailSenderService.sendSimpleEmail(patient.getEmail(), "Hello,you have registered for an online consultation" +
                        "You are registered" + doctor.getName() + "to the doctor",
                "doctor data zoom:" + doctor.getPassword() + "password"
                        + doctor.getZoomId() + "id");

    }

    @Override
    public List<Patient> findDoctorPatientsFromAppointments(int id, String searchText) {
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorId(id);
        List<Patient> patients = getPatientsFromAppointments(doctorAppointments);
        if (!searchText.equals("") && !searchText.equalsIgnoreCase("null")) {
            patients = patients.stream()
                    .filter(patient -> patient.getSurname().contains(searchText))
                    .toList();
        }
        return patients;
    }

    private List<Patient> getPatientsFromAppointments(List<Appointment> doctorAppointments) {
        return doctorAppointments.stream()
                .map(Appointment::getPatient)
                .toList();

    }
}
