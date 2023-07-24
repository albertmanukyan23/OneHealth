package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.mapper.PatientMapper;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthcommon.util.ImageDownloader;
import com.example.onehealthrest.service.PatientService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    @Value("${site.url}")
    private String siteUrl;

    private final PatientRepository patientRepository;
    private final UserService userService;
    private final ImageDownloader imageDownloader;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PatientDto save(Patient patient) throws IOException {
        patient.setRegisDate(new Date());
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
        return patientMapper.map(patient);
    }

    @Override
    public List<PatientDto> getPatientsDtoList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Patient> content = patientRepository.findAll(pageable).getContent();
        return patientMapper.mapListToDtos(content);
    }

    @Override
    @Transactional
    public Optional<Patient>  update(PatientRegisterDto patientRegisterDto, int id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isPresent()) {
            Patient patientDbData = byId.get();
            if (patientRepository.findByEmail(patientRegisterDto.getEmail()).isEmpty() || patientRegisterDto.getEmail().equals(patientDbData.getEmail())) {
                patientDbData.setName(patientRegisterDto.getName());
                patientDbData.setSurname(patientRegisterDto.getSurname());
                patientDbData.setEmail(patientRegisterDto.getEmail());
                patientDbData.setPassword(passwordEncoder.encode(patientRegisterDto.getPassword()));
                patientDbData.setBirthDate(patientRegisterDto.getBirthDate());
                patientDbData.setRegion(patientRegisterDto.getRegion());
                patientDbData.setNation(patientRegisterDto.getNation());
                patientDbData.setAddress(patientRegisterDto.getAddress());
               return Optional.of(patientRepository.save(patientDbData));
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            patientRepository.deleteById(id);
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public StringBuilder checkValidation(BindingResult bindingResult) {
        StringBuilder errorBuilder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> errorBuilder.append(error.getDefaultMessage()).append("\n"));
        }
        return errorBuilder;
    }
}
