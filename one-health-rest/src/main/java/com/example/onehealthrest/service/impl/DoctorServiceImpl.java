package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.dto.UpdateDoctor;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.mapper.DoctorMapper;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthrest.service.DoctorService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public DoctorDtoResponse save(Doctor doctor) {
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        userService.registerUser(doctor);
        DoctorDtoResponse doctorDto = doctorMapper.map(doctor);
        return doctorDto;
    }

    @Override
    public Optional<Doctor> findById(int id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Doctor update(Doctor doctor, UpdateDoctor userFromDb) {
        Optional<Doctor> byEmail = doctorRepository.findByEmail(doctor.getEmail());
        Doctor doctorDb = byEmail.get();
        if (doctor.getEmail().isEmpty() ||
                doctor.getEmail().equals(doctorDb.getEmail())) {
            if (doctor.getName() != null && !doctor.getName().isEmpty()) {
                doctorDb.setName(doctor.getName());
            }
            if (doctor.getSurname() != null && !doctor.getSurname().isEmpty()) {
                doctorDb.setName(doctor.getSurname());
            }
            if (doctor.getEmail() != null && !doctor.getEmail().isEmpty()) {
                doctorDb.setName(doctor.getEmail());
            }
            if (doctor.getPassword() != null && !doctor.getPassword().isEmpty()) {
                doctorDb.setPassword(passwordEncoder.encode(doctor.getPassword()));
            }
            if (doctor.getSpeciality() != null && !doctor.getSpeciality().isEmpty()) {
                doctorDb.setSpeciality(doctor.getSpeciality());
            }
            if (doctor.getBirthDate() != null) {
                doctorDb.setBirthDate(doctor.getBirthDate());
            }
            if (doctor.getPhoneNumber() != null && !doctor.getPhoneNumber().isEmpty()) {
                doctorDb.setPhoneNumber(doctor.getPhoneNumber());
            }
            if (doctor.getZoomPassword() != null && !doctor.getZoomPassword().isEmpty()) {
                doctorDb.setName(doctor.getName());
            }
            return doctorDb;
        }
        return null;
    }
}