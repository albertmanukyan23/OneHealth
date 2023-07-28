package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.mapper.PatientMapper;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthrest.service.PatientService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Value("${site.url}")
    private String siteUrl;

    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PatientDto save(Patient patient)  {
        patient.setRegisDate(new Date());
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
        return patientMapper.map(patient);
    }

    @Override
    public List<PatientDto> getPatientsDtoList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Patient> content = patientRepository.findAll(pageRequest).getContent();
        return patientMapper.mapListToDtos(content);
    }

    @Override
    @Transactional
    public Optional<Patient> update(PatientRegisterDto patientRegisterDto, int id) {
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
                log.info("Patient with the " + patientDbData.getId() + " id was updated");
                return Optional.of(patientRepository.save(patientDbData));
            }
        }
        log.info("Patient can not be updated");
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            patientRepository.deleteById(id);
            log.info("Patient with the " + id + " id was deleted");
            isDeleted = true;
        }
        log.info("Patient with the " + id + " id can not be deleted");
        return isDeleted;
    }

    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientRepository.findById(id);
    }


}
