package com.example.onehealthmvc.service;
import com.example.onehealthcommon.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> getPatient();

    Page<Patient> getPatientPag(Pageable pageable);

    void update(Patient patient, MultipartFile multipartFile) ;

    Optional<Patient> findPatientById(int id);

    Page<Patient> getPatientPageData(Optional<Integer> page, Optional<Integer> size);

    List<Integer> getPageNumbers(int totalPages);

    void save(MultipartFile multipartFile, Patient patient) ;

    Optional<Patient> findByEmail(String email);

}
