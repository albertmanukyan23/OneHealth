package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.dto.PatientSearchDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.exception.EntityNotFoundException;
import com.example.onehealthcommon.component.PatientFilterManager;
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
    private final PatientFilterManager patientFilterManager;


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
            throw new EntityNotFoundException("Patient List IsEmpty");
        } else {
            return patientMapper.mapListToDtos(content);

        }
    }

    /**
     * Updates an existing Patient entity in the system with the provided information.
     *
     * @param patientRegisterDto The DTO containing the new details for updating the Patient.
     * @param id                 The unique identifier of the Patient to be updated.
     * @return Optional<Patient> An Optional containing the updated Patient entity if the update is successful,
     *                          or an empty Optional if the Patient with the given id does not exist or the email
     *                          provided in the DTO is already in use by another Patient.
     * @throws EntityNotFoundException If no Patient is found with the given id in the database.
     *
     */

    @Override
    @Transactional
    public Optional<Patient> update(PatientRegisterDto patientRegisterDto, int id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("ById with " + id + " does not exist");
        } else {
            Patient patientDbData = byId.get();
            if (patientRepository.findByEmail(patientRegisterDto.getEmail()).isEmpty()
                    || patientRegisterDto.getEmail().equals(patientDbData.getEmail())) {
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
     * Deletes a Patient entity from the system with the given id.
     *
     * @param id The unique identifier of the Patient to be deleted.
     * @return boolean True if the Patient is successfully deleted, otherwise false.
     * @throws EntityNotFoundException If no Patient is found with the given id in the database.
     */

    @Override
    public boolean delete(int id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            throw new EntityNotFoundException("ById with " + id + " does not exist");
        } else {
            patientRepository.deleteById(id);
            log.info("Patient with the " + id + " id was deleted");
            return true;
        }
    }


    @Override
    public Optional<Patient> findPatientById(int id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("ById with " + id + " does not exist");
        } else {
            return byId;
        }
    }

    @Override
    public List<PatientDto> search(int page, int size, PatientSearchDto bookSearchDto)
    {
        List<Patient> all = patientFilterManager.searchPatientsByFilter(page, size, bookSearchDto);
        return patientMapper.mapListToDtos(all);
    }

}
