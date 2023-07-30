package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.component.DoctorFilterManager;
import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.dto.DoctorSearchDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.exception.EntityNotFoundException;
import com.example.onehealthcommon.mapper.DoctorMapper;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthrest.service.DoctorService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final DoctorFilterManager doctorFilterManager;

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
     * Updates an existing Doctor entity in the system with the provided information.
     *
     * @param creatDoctorRequestDto The DTO containing the new details for updating the Doctor.
     * @param id                    The unique identifier of the Doctor to be updated.
     * @return Optional<Doctor>     An Optional containing the updated Doctor entity if the update is successful,
     * or an empty Optional if the Doctor with the given id does not exist or the email
     * provided in the DTO is already in use by another Doctor.
     * @throws EntityNotFoundException If no Doctor is found with the given id in the database.
     */

    @Override
    public Optional<Doctor> update(CreatDoctorRequestDto creatDoctorRequestDto, int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("ById with " + id + " does not exist");
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
                departmentById.ifPresentOrElse(doctorDb::setDepartment, null);
                doctorDb.setZoomId(creatDoctorRequestDto.getZoomId());
                doctorDb.setPassword(creatDoctorRequestDto.getZoomPassword());
                log.info("Doctor with the " + doctorDb.getId() + " id was updated");
                return Optional.of(doctorRepository.save(doctorDb));
            }
            return Optional.empty();
        }
    }


    @Override
    public List<DoctorDtoResponse> searchDoctor(int size, int page, DoctorSearchDto doctorSearchDto) {
        List<Doctor> doctors = doctorFilterManager.searchDoctorsByFilter(page, size, doctorSearchDto);
        return doctorMapper.mapListDto(doctors);
    }


    @Override
    public boolean deleteById(int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("ById with " + id + " does not exist");
        } else {
            doctorRepository.deleteById(id);
            log.info("Doctor with the " + id + " id was deleted");
            return true;
        }
    }

    @Override
    public Optional<Doctor> getDoctorById(int id) {
        Optional<Doctor> byId = doctorRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("ById with " + id + " does not exist");
        }
        return byId;
    }


    @Override
    public Optional<Doctor> findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
}