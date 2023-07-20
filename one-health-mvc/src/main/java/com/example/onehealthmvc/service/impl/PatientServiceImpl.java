package com.example.onehealthmvc.service.impl;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthmvc.service.PatientService;
import com.example.onehealthmvc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;


    @Value("${site.url}")
    private String siteUrl;

    @Override
    public List<Patient> getPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Page<Patient> getPatientPag(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public void update(Patient patient, MultipartFile multipartFile) throws IOException {
        Optional<Patient> byId = patientRepository.findById(patient.getId());
        if (byId.isPresent()) {
            Patient patientDbData = byId.get();
            if (patientRepository.findByEmail(patient.getEmail()).isEmpty() || patient.getEmail().equals(patientDbData.getEmail())) {
                patientDbData.setName(patient.getName());
                patientDbData.setSurname(patient.getSurname());
                patientDbData.setEmail(patient.getEmail());
                patientDbData.setPassword(passwordEncoder.encode(patient.getPassword()));
                patientDbData.setBirthDate(patient.getBirthDate());
                patientDbData.setPicName(patient.getPicName());
                patientDbData.setRegion(patient.getRegion());
                patientDbData.setNation(patient.getNation());
                patientDbData.setAddress(patient.getAddress());
                patient.setRegisDate(new Date());
                imageDownloader.saveProfilePicture(multipartFile, patient);
                patientRepository.save(patientDbData);
            }
        }
    }

    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public Page<Patient> getPatientPageData(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return patientRepository.findAll(pageable);
    }

    @Override
    public List<Integer> getPageNumbers(int totalPages) {
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return null;
    }
    @Override
    @Transactional
    public void save(MultipartFile multipartFile, Patient patient) throws IOException {
        patient.setRegisDate(new Date());
        imageDownloader.saveProfilePicture(multipartFile, patient);
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }



}
