package com.example.onehealth.service.impl;

import com.example.onehealth.entity.Doctor;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.repository.DoctorRepository;
import com.example.onehealth.service.DoctorService;
import com.example.onehealth.service.EmailSenderService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
        sendDoctorRegistrationMessage(doctor);
        userService.registerUser(doctor);
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
    public List<Doctor> searchDoctorsByKey(String searchText) {
        List<Doctor> doctors;
        if (searchText.equals("") || searchText.equalsIgnoreCase("null")) {
            doctors = doctorRepository.findAll();
        } else {
            doctors = doctorRepository.findBySurnameContaining(searchText);
        }
        return doctors;
    }


    public void sendDoctorRegistrationMessage(Doctor doctor) {
        emailSenderService.sendSimpleEmail(doctor.getEmail(), "You password for Log in OneHealth",
                "password: " + doctor.getPassword() + "\n Please don't lose it.");

    }
    
}
