package com.example.onehealth.service;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.User;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> getPatient();
    Optional<User> findByEmail(String email);
    void update(Patient patient);
    Optional<Patient> findPatientById(int id);
}
