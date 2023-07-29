package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.exception.EntityNotFoundException;
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
    public PatientDto save(Patient patient) {
        patient.setRegisDate(new Date());
        patient.setUserType(UserType.PATIENT);
        userService.registerUser(patient);
        return patientMapper.map(patient);
    }

    @Override
    public List<PatientDto> getPatientsDtoList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Patient> content = patientRepository.findAll(pageable).getContent();
        if (content.isEmpty()) {
            try {
                throw new EntityNotFoundException("Patient List IsEmpty");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return patientMapper.mapListToDtos(content);
    }

    /**
     * Updates the details of a patient with the specified ID.
     *
     * @param patientRegisterDto The DTO containing the updated details for the patient.
     * @param id                 The ID of the patient to be updated.
     * @return An optional containing the updated patient entity if found and updated successfully,
     *         or an empty optional if the patient with the given ID does not exist or the email is already taken.
     */

    @Override
    @Transactional
    public Optional<Patient> update(PatientRegisterDto patientRegisterDto, int id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isPresent()) {
            try {
                throw new EntityNotFoundException("ById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
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
            log.info("Patient can not be updated");
            return Optional.empty();
        }
    }

    /**
     * Deletes a patient with the specified ID.
     *
     * @param id The ID of the patient to be deleted.
     * @return A boolean indicating whether the patient was successfully deleted or not.
     */

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            try {
                throw new EntityNotFoundException("ById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            patientRepository.deleteById(id);
            log.info("Patient with the " + id + " id was deleted");
           return isDeleted = true;
        }
    }


        @Override
        public Optional<Patient> findPatientById ( int id){
            Optional<Patient> byId = patientRepository.findById(id);
            if (byId.isEmpty()){
                try {
                    throw new EntityNotFoundException("ById with " + id + " does not exist");
                } catch (EntityNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return byId;
        }


    }
