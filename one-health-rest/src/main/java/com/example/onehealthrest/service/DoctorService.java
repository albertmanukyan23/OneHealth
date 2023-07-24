package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.dto.UpdateDoctor;
import com.example.onehealthcommon.entity.Doctor;

import java.util.Optional;

public interface DoctorService {
    Optional<Doctor> findByEmail(String email);

    DoctorDtoResponse save(Doctor doctor);

    Optional<Doctor> findById(int id);

    Doctor update(Doctor doctor, UpdateDoctor userFromDb);

}
