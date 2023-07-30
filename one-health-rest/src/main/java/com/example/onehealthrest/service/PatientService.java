package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.dto.PatientSearchDto;
import com.example.onehealthcommon.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientDto save(Patient patient);

    List<PatientDto> getPatientsDtoList(int page, int size);

    Optional<Patient> update(PatientRegisterDto patientRegisterDto, int id);

    boolean delete(int id);

    Optional<Patient> findPatientById(int id);

    List<PatientDto> search(int page, int size, PatientSearchDto bookSearchDto);
}
