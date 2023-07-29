package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.AppointmentDto;
import com.example.onehealthcommon.dto.CreateAppointmentDto;
import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.entity.Appointment;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthcommon.repository.DoctorRepository;
import com.example.onehealthcommon.repository.PatientRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class, DepartmentMapper.class})
@Component
public abstract class AppointmentMapper {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Mapping(target = "doctorName", source = "doctor.name")
    public abstract PatientAppointmentDto map(Appointment appointment);

    public abstract List<PatientAppointmentDto> map(List<Appointment> appointments);

    public abstract List<AppointmentDto> mapToAppointmentDtoList(List<Appointment> appointments);

    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "getDoctorFromDb")
    @Mapping(target = "patient", source = "patientId", qualifiedByName = "getPatientFromDb")
    @Mapping(target = "department", source = "departmentId", qualifiedByName = "getDepartmentFromDb")
    public abstract Appointment map(CreateAppointmentDto dto);

    @Mapping(target = "doctorDto", source = "doctor")
    @Mapping(target = "patientDto", source = "patient")
    @Mapping(target = "departmentDto", source = "department")
    public abstract AppointmentDto mapToDto(Appointment appointment);

    @Named("getDoctorFromDb")
    protected Doctor getDoctorFromDb(int doctorId) {
        return doctorRepository.findById(doctorId).orElse(null);
    }

    @Named("getPatientFromDb")
    protected Patient getPatientFromDb(int patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    @Named("getDepartmentFromDb")
    protected Department getDepartmentFromDb(int departmentId) {
        return departmentRepository.findById(departmentId).orElse(null);
    }
}
