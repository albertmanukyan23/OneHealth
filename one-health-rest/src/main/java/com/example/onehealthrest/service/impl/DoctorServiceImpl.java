package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.dto.UpdateDoctor;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.mapper.DoctorMapper;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthrest.service.DoctorService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
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
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public DoctorDtoResponse save(Doctor doctor) {
        doctor.setRegisDate(new Date());
        doctor.setUserType(UserType.DOCTOR);
        userService.registerUser(doctor);
        DoctorDtoResponse doctorDto = doctorMapper.map(doctor);
        return doctorDto;
    }

    @Override
    public Optional<Doctor> update(CreatDoctorRequestDto creatDoctorRequestDto, int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isPresent()) {
            Doctor doctorDb = byId.get();
            if (doctorRepository.findByEmail(creatDoctorRequestDto.getEmail()).isEmpty()
                    || creatDoctorRequestDto.getEmail().equals(doctorDb.getEmail())) {
                doctorDb.setName(creatDoctorRequestDto.getName());
                doctorDb.setSurname(creatDoctorRequestDto.getSurname());
                doctorDb.setEmail(creatDoctorRequestDto.getEmail());
                doctorDb.setPassword(passwordEncoder.encode(creatDoctorRequestDto.getPassword()));
                doctorDb.setSpeciality(creatDoctorRequestDto.getSpeciality());
                doctorDb.setBirthDate(creatDoctorRequestDto.getBirthDate());
                doctorDb.setPhoneNumber(creatDoctorRequestDto.getPhoneNumber());
                doctorDb.setDepartment(creatDoctorRequestDto.getDepartment());
                doctorDb.setZoomId(creatDoctorRequestDto.getZoomId());
                doctorDb.setPassword(creatDoctorRequestDto.getZoomPassword());
                return Optional.of(doctorRepository.save(doctorDb));
            }

        }
        return Optional.empty();
    }


    @Override
    public StringBuilder checkValidation(BindingResult bindingResult) {
        StringBuilder errorBuilder = new StringBuilder();
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> errorBuilder.append(error.getDefaultMessage()).append("\n"));
        }
        return errorBuilder;
    }

    @Override
    public Optional<Doctor> findById(int id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Optional<Doctor> getDoctorById(int id) {
        return doctorRepository.findById(id);
    }

    @Override
    public List<DoctorDtoResponse> getDoctorList(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Doctor> content = doctorRepository.findAll(pageable).getContent();
        return doctorMapper.mapListDto(content);
    }

    @Override
    public boolean deleteById(int id) {
        boolean delete = false;
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isPresent()) {
            doctorRepository.findById(id);
            delete = true;
        }
        return delete;
    }
}