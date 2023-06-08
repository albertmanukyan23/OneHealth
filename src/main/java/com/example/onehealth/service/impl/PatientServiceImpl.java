package com.example.onehealth.service.impl;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.User;
import com.example.onehealth.repository.PatientRepository;
import com.example.onehealth.service.PatientService;
import com.example.onehealth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public List<Patient> getPatient() {
        return patientRepository.findAll();
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void update(Patient patient) {
        Optional<Patient> byId = patientRepository.findById(patient.getId());
        if (byId.isPresent()) {
            Patient updateDB = byId.get();
            if (patientRepository.findByEmail(patient.getEmail()).isEmpty()) {
                updateDB.setName(patient.getName());
                updateDB.setSurname(patient.getSurname());
                updateDB.setEmail(patient.getEmail());
                updateDB.setPassword(passwordEncoder.encode(patient.getPassword()));
                updateDB.setBirthDate(patient.getBirthDate());
                updateDB.setPicName(patient.getPicName());
                updateDB.setRegion(patient.getRegion());
                updateDB.setNation(patient.getNation());
                updateDB.setAddress(patient.getAddress());
                patientRepository.save(updateDB);
            }
        }
    }
    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientRepository.findById(id);
    }
}
