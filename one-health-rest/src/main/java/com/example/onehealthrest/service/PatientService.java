package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.entity.Patient;


import java.io.IOException;

public interface PatientService {
    PatientDto save(Patient patient) throws IOException;
}
