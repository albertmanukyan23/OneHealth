package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.exception.EntityConflictException;
import com.example.onehealthcommon.exception.EntityNotFoundException;
import com.example.onehealthcommon.mapper.DoctorMapper;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthrest.service.DoctorService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public DoctorDtoResponse save(Doctor doctor) {
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        userService.registerUser(doctor);
        DoctorDtoResponse doctorDto = doctorMapper.map(doctor);
        log.info("failed to register");
        return doctorDto;
    }

    /**
     * Updates the details of a doctor with the specified ID.
     *
     * @param creatDoctorRequestDto The DTO containing the updated details for the doctor.
     * @param id                    The ID of the doctor to be updated.
     * @return An optional containing the updated doctor entity if found and updated successfully,
     *         or an empty optional if the doctor does not exist or the update is not allowed due to email conflict.
     */

    @Override
    public Optional<Doctor> update(CreatDoctorRequestDto creatDoctorRequestDto, int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isEmpty()) {
            try {
                throw new EntityNotFoundException("ById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            Doctor doctorDb = byId.get();
            if (doctorRepository.findByEmail(creatDoctorRequestDto.getEmail()).isEmpty()
                    || !creatDoctorRequestDto.getEmail().equals(doctorDb.getEmail())) {
                doctorDb.setName(creatDoctorRequestDto.getName());
                doctorDb.setSurname(creatDoctorRequestDto.getSurname());
                doctorDb.setEmail(creatDoctorRequestDto.getEmail());
                doctorDb.setPassword(passwordEncoder.encode(creatDoctorRequestDto.getPassword()));
                doctorDb.setSpeciality(creatDoctorRequestDto.getSpeciality());
                doctorDb.setBirthDate(creatDoctorRequestDto.getBirthDate());
                doctorDb.setPhoneNumber(creatDoctorRequestDto.getPhoneNumber());
                Optional<Department> departmentById = departmentRepository.findById(creatDoctorRequestDto.getDepartmentId());
                departmentById.ifPresentOrElse(department -> doctorDb.setDepartment(department), null);
                doctorDb.setZoomId(creatDoctorRequestDto.getZoomId());
                doctorDb.setPassword(creatDoctorRequestDto.getZoomPassword());
                log.info("Doctor with the " + doctorDb.getId() + " id was updated");
                return Optional.of(doctorRepository.save(doctorDb));
            }
            return Optional.empty();
        }
    }


    @Override
    public List<DoctorDtoResponse> getDoctorList(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Doctor> content = doctorRepository.findAll(pageable).getContent();
        if (content.isEmpty()) {
            try {
                throw new EntityNotFoundException("Doctor List IsEmpty");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return doctorMapper.mapListDto(content);
    }

    @Override
    public boolean deleteById(int id) {
        boolean delete = false;
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isEmpty()) {
            try {
                throw new EntityNotFoundException("ById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            doctorRepository.deleteById(id);
            log.info("Doctor with the " + id + " id was deleted");
            return delete = true;
        }
    }

    @Override
    public Optional<Doctor> getDoctorById(int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isEmpty()) {
            try {
                throw new EntityNotFoundException("ById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return byId;
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
}