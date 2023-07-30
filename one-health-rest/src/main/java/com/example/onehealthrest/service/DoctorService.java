package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.dto.DoctorSearchDto;
import com.example.onehealthcommon.entity.Doctor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    DoctorDtoResponse save(Doctor doctor);

    Optional<Doctor> update(CreatDoctorRequestDto creatDoctorRequestDto, int id);

    StringBuilder checkValidation(BindingResult bindingResult);

    List<DoctorDtoResponse> searchDoctor(int page, int size, DoctorSearchDto doctorSearchDto);

    boolean deleteById(int id);

    Optional<Doctor> getDoctorById(int id);

    Optional<Doctor> findByEmail(String email);
}

