package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    PatientDto save(Patient patient) throws IOException;

    List<PatientDto> getPatientsDtoList(int page, int size);

    Patient update(PatientRegisterDto patientRegisterDto, int id);

    boolean delete(int id);

    Optional<Patient> findPatientById(int id);
}
