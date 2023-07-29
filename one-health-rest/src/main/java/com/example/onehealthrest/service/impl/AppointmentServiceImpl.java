package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.EmailSenderService;
import com.example.onehealthcommon.dto.AppointmentDto;
import com.example.onehealthcommon.dto.CreateAppointmentDto;
import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.entity.*;
import com.example.onehealthcommon.mapper.AppointmentMapper;
import com.example.onehealthcommon.repository.AppointmentRepository;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthrest.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EmailSenderService emailSenderService;


    @Override
    public List<PatientAppointmentDto> getPatientAppointments(User user) {
        return appointmentMapper.map(appointmentRepository.findAllByPatientId(user.getId()));
    }

    @Override
    public List<AppointmentDto> getAppointmentDtos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Appointment> content = appointmentRepository.findAll(pageable).getContent();
        return appointmentMapper.mapToAppointmentDtoList(content);
    }

    /**
     * Creates a new appointment for the current user with the details provided in the {@code appointmentDto}.
     * The method checks if the provided patient and doctor exist in the repository, and if the appointment is valid
     * based on certain criteria. If the appointment is valid, it will be saved to the repository, and the patient will
     * be notified about the online registration.
     *
     * @param currentUser   The current user creating the appointment.
     * @param appointmentDto The data transfer object containing the details of the new appointment.
     * @return An optional containing the DTO representation of the created appointment if successful, or an empty optional
     *         if the appointment creation failed due to invalid data or other reasons.
     */

    @Override
    @Transactional
    public Optional<AppointmentDto> createAppointment(User currentUser, CreateAppointmentDto appointmentDto) {
        Optional<Patient> patientOptional = patientRepository.findById(currentUser.getId());
        Optional<Doctor> doctorOptional = doctorRepository.findById(appointmentDto.getDoctorId());
        Appointment appointment = appointmentMapper.map(appointmentDto);
        if (isValidAppointment(patientOptional, doctorOptional, appointment)) {
            notifyOnlineRegistration(appointment);
            saveAppointment(appointment);
            log.info("Appointment has been created for the user with " + currentUser.getId() + "id");
            return Optional.of(appointmentMapper.mapToDto(appointment));
        }
        log.info("Appointment creating failed for user with " + currentUser.getId() + "id");
        return Optional.empty();
    }

    /**
     * Checks if the specified doctor is available for a new appointment at the provided start time.
     * The method retrieves all existing appointments for the doctor and checks for any scheduling conflicts
     * with the new appointment's start time. If there is a scheduling conflict, the doctor is considered
     * unavailable for the new appointment.
     *
     * @param appointment The new appointment to be checked for doctor availability.
     * @return {@code true} if the doctor is available for the new appointment, {@code false} otherwise.
     */

    public boolean isDoctorAvailableForAppointment(Appointment appointment) {
        boolean isDoctorAvailable = true;
        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorId(appointment.getDoctor().getId());
        LocalDateTime newAppointmentStartDate = appointment.getStartTime();
        for (Appointment existingAppointment : doctorAppointments) {
            LocalDateTime existedAppointmentStartDate = existingAppointment.getStartTime().minusMinutes(30);
            if (newAppointmentStartDate.isAfter(existedAppointmentStartDate) &&
                    newAppointmentStartDate.isBefore(existingAppointment.getEndTime())) {
                log.info("isDoctorAvailableForAppointment() inside AppointmentServiceImpl has returned false for user by " + appointment.getPatient().getId() + " id");
                isDoctorAvailable = false;
                break;
            }
        }
        return isDoctorAvailable;
    }

    /**
     * Cancels an appointment with the specified appointment ID, performed by the given user.
     * The method checks if the appointment is able to be canceled, and if so, deletes the appointment
     * from the repository. If the user is a doctor, a notification message will be sent to the patient.
     *
     * @param appointmentId The ID of the appointment to be canceled.
     * @param currentUser   The user performing the cancellation.
     * @return {@code true} if the appointment was successfully canceled, {@code false} otherwise.
     */

    @Override
    @Transactional
    public boolean cancelAppointmentById(int appointmentId, User currentUser) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        boolean isAppointmentCanceled = false;
        if (isAppointmentAbleToBeCanceled(appointmentId, currentUser)) {
            appointmentRepository.deleteById(appointmentId);
            if (currentUser.getUserType() == UserType.DOCTOR) {
                sendAppointmentCancelMessageToPatient(optional.get().getPatient());
            }
            log.info("Appointment has been canceled by the user with " + currentUser.getId() + " id");
            isAppointmentCanceled = true;
        }
        log.info("Appointment canceling failed ");

        return isAppointmentCanceled;
    }

    private boolean isAppointmentAbleToBeCanceled(int appointmentId, User currentUser) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        int currentUserId = currentUser.getId();
        return optional.isPresent() && (currentUserId == optional.get().getPatient().getId() || currentUserId == optional.get().getDoctor().getId());
    }

    /**
     * Checks if the start time of the appointment falls within the working hours.
     * The working hours are considered between 8:00 AM (inclusive) and 6:00 PM (exclusive).
     *
     * @param appointment The appointment to check.
     * @return {@code true} if the appointment's start time is within the working hours, {@code false} otherwise.
     */

    private boolean isAppointmentBetweenWorkingHours(Appointment appointment) {
        return appointment.getStartTime().getHour() >= 8 && appointment.getStartTime().getHour() < 18;
    }

    private boolean isValidAppointment(Optional<Patient> patientOptional, Optional<Doctor> doctorOptional,
                                       Appointment appointment) {
        return patientOptional.isPresent() && doctorOptional.isPresent() &&
                isDoctorAvailableForAppointment(appointment)
                && isAppointmentBetweenWorkingHours(appointment);
    }

    private void saveAppointment(Appointment appointment) {
        LocalDateTime appointmentEndTime = appointment.getStartTime().plusMinutes(30);
        appointment.setEndTime(appointmentEndTime);
        appointmentRepository.save(appointment);
    }

    /**
     * Notifies the patient and doctor about an online registration for the given appointment.
     * If the appointment's registration type is set to {@link RegisterType#ONLINE},
     * a notification message will be sent to the patient and the doctor.
     *
     * @param appointment The appointment for which to notify about online registration.
     */

    private void notifyOnlineRegistration(Appointment appointment) {
        if (appointment.getRegisterType() == RegisterType.ONLINE) {
            sendOnlineRegistrationMessage(appointment.getPatient(), appointment.getDoctor());
        }
    }


    public void sendAppointmentCancelMessageToPatient(Patient patient) {
        emailSenderService.sendSimpleEmail(patient.getEmail(), "Appointment was cancelled",
                "Your appointment has been canceled by the doctor," +
                        " we ask for your forgiveness, try to book a consultation again for another day.");

        log.info("Appointment canceling message has been send to the user with " + patient.getId() + " id");

    }


    public void sendOnlineRegistrationMessage(Patient patient, Doctor doctor) {
        emailSenderService.sendSimpleEmail(patient.getEmail(), doctor.doctorEmailSubject(), doctor.getEmailBody());

        log.info("Online Registration message  has been send to the user with " + patient.getId() + " id");
    }
}
