package com.example.onehealthmvc.service.impl;

import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.exception.ImageProcessingException;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthmvc.service.PatientService;
import com.example.onehealthmvc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    //Updates the patient's information and profile picture if the email is unique or remains unchanged
    @Override
    @Transactional
    public void update(Patient patient, MultipartFile multipartFile) {
        try {
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
                    log.info("Patient has been updated");
                }
            }
            log.info("Patient update()  failed");

        } catch (IOException e) {
            log.info("Catch ImageProcessingException() exception during the updating patient with id " + patient.getId());
            throw new ImageProcessingException("Image uploading failed");
        }

    }

    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientRepository.findById(id);
    }

    //Generates data for patient page
    @Override
    public Page<Patient> getPatientPageData(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return patientRepository.findAll(pageable);
    }

    //Generates a list of page numbers for pagination

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
    public void save(MultipartFile multipartFile, Patient patient) {
        try {
            patient.setRegisDate(new Date());
            imageDownloader.saveProfilePicture(multipartFile, patient);
            patient.setUserType(UserType.PATIENT);
            userService.registerUser(patient);

        } catch (IOException e) {
            log.info("Catch ImageProcessingException() exception during the saving patient with id " + patient.getId());
            throw new ImageProcessingException("Image uploading failed");
        }

    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }


}
