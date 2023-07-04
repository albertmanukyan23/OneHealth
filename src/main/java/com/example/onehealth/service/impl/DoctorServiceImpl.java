package com.example.onehealth.service.impl;

import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.repository.DoctorRepository;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.EmailSenderService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;
    private final EmailSenderService emailSenderService;

    @Value("${site.url}")
    private String siteUrl;

    @Override
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> findDoctorById(int id) {
        return doctorRepository.findById(id);
    }

    @Override
    @Transactional
    public void update(Doctor doctor, MultipartFile multipartFile) throws IOException {
        Optional<Doctor> byId = doctorRepository.findById(doctor.getId());
        if (byId.isPresent()) {
            Doctor doctorFromDb = byId.get();
            if (doctorRepository.findByEmail(doctor.getEmail()).isEmpty() || doctor.getEmail().equals(doctorFromDb.getEmail())) {
                doctorFromDb.setName(doctor.getName());
                doctorFromDb.setSurname(doctor.getSurname());
                doctorFromDb.setEmail(doctor.getEmail());
                doctorFromDb.setPassword(passwordEncoder.encode(doctor.getPassword()));
                doctorFromDb.setBirthDate(doctor.getBirthDate());
                doctorFromDb.setDepartment(doctor.getDepartment());
                doctorFromDb.setPicName(doctor.getPicName());
                doctorFromDb.setSpeciality(doctor.getSpeciality());
                doctorFromDb.setPhoneNumber(doctor.getPhoneNumber());
                doctorFromDb.setZoomId(doctor.getZoomId());
                doctorFromDb.setZoomPassword(doctor.getZoomPassword());
                imageDownloader.saveProfilePicture(multipartFile, doctorFromDb);
                doctorRepository.save(doctorFromDb);
            }
        }
    }

    @Override
    public Page<Doctor> getDoctorPage(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void registerDoctor(Doctor doctor, MultipartFile multipartFile) throws IOException {
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        imageDownloader.saveProfilePicture(multipartFile, doctor);
        UUID token = UUID.randomUUID();
        doctor.setToken(token.toString());
        doctor.setEnabled(false);
        userService.registerUser(doctor);
        sendDoctorRegistrationMessage(doctor.getId());
        verifyAccountWithEmail(doctor.getId());
    }

    @Override
    public Page<Doctor> getDoctorPageData(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return doctorRepository.findAll(pageable);
    }

    @Override
    public List<Integer> getNumbersPage(int totalPages) {
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public void verifyAccount(String email, String token) {
        Optional<Doctor> byEmail = doctorRepository.findByEmail(email);
        if (byEmail.get().getToken().equals(token)) {
            Doctor doctorDb = byEmail.get();
            doctorDb.setEnabled(true);
            doctorDb.setToken(null);
            doctorRepository.save(doctorDb);
        }
    }

    @Override
    public void confirmationMessage(String email) {
        Optional<Doctor> byEmail = doctorRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            Doctor doctor = byEmail.get();
            UUID token = UUID.randomUUID();
            doctor.setToken(token.toString());
            doctorRepository.save(doctor);
            emailSenderService.sendSimpleEmail(doctor.getEmail(),
                    "Welcome", "Hi" + doctor.getName() +
                            "\n" + "Confirm to rest password " +
                            siteUrl + "/doctor/change-password-page?email=" + doctor.getEmail() + "&token=" + doctor.getToken());
        }
    }

    @Override
    public void changePassword(String email, String token) {
        Optional<Doctor> byEmail = doctorRepository.findByEmail(email);
        if (byEmail.get().getToken().equals(token)) {
            Doctor doctor = byEmail.get();
            doctor.setToken(null);
            doctorRepository.save(doctor);
        }
    }
    @Override
    public void updatePassword(String email, String token, String password, String passwordRepeat) {
        Optional<Doctor> byEmail = doctorRepository.findByEmail(email);
        if (byEmail.isPresent() && byEmail.get().isEnabled()) {
            if (password.equals(passwordRepeat) && byEmail.get().getToken() == null) {
                Doctor doctor = byEmail.get();
                doctor.setPassword(passwordEncoder.encode(password));
                doctorRepository.save(doctor);
            }
        }
    }

    @Async
    public void sendDoctorRegistrationMessage(int id) {
        Optional<Doctor> doctorFromDb = doctorRepository.findById(id);
        if (doctorFromDb.isPresent()) {
            Doctor doctor = doctorFromDb.get();
            emailSenderService.sendSimpleEmail(doctor.getEmail(), "You password for Log in OneHealth",
                    "password: " + doctor.getPassword() + "\n Please don't lose it.");
        }
    }

    @Async
    public void verifyAccountWithEmail(int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isPresent()) {
            Doctor doctor = byId.get();
            emailSenderService.sendSimpleEmail(doctor.getEmail(),
                    "Welcome", "Hi" + doctor.getName() +
                            "\n" + "Please verify your email by clicking on this url " +
                            siteUrl + "/doctor/verify?email=" + doctor.getEmail() + "&token=" + doctor.getToken());
        }
    }
}
