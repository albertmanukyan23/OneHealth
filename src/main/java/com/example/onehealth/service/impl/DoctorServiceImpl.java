package com.example.onehealth.service.impl;

import com.example.onehealth.entity.Doctor;
import com.example.onehealth.repository.DoctorRepository;
import com.example.onehealth.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> findDoctorById(int id) {
        return doctorRepository.findById(id);
    }

    @Override
    public void update(Doctor doctor) {
        Optional<Doctor> byId = doctorRepository.findById(doctor.getId());
        if (byId.isPresent()) {
            Doctor doctorFromDb = byId.get();
            if (doctorRepository.findByEmail(doctor.getEmail()).isEmpty()) {
                doctorFromDb.setName(doctor.getName());
                doctorFromDb.setSurname(doctor.getSurname());
                doctorFromDb.setEmail(doctor.getEmail());
                doctorFromDb.setPassword(passwordEncoder.encode(doctor.getPassword()));
                doctorFromDb.setBirthDate(doctor.getBirthDate());
                doctorFromDb.setPicName(doctor.getPicName());
                doctorFromDb.setSpeciality(doctor.getSpeciality());
                doctorFromDb.setPhoneNumber(doctor.getPhoneNumber());
                doctorRepository.save(doctorFromDb);
            }
        }
    }

    @Override
    public Page<Doctor> getDoctorPage(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }




}
