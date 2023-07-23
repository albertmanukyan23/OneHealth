package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.mapper.PatientMapper;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthrest.service.PatientService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    @Value("${site.url}")
    private String siteUrl;

    private final PatientRepository patientRepository;
    private final UserService userService;
    private final ImageDownloader imageDownloader;
    private final PatientMapper patientMapper;

    @Override
    public PatientDto save(Patient patient) throws IOException {
        patient.setRegisDate(new Date());
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
        PatientDto patientDto = patientMapper.map(patient);
        patientDto.setPicName(siteUrl + "/getImage?picName=" + patient.getPicName());
        return patientDto;
    }
}
