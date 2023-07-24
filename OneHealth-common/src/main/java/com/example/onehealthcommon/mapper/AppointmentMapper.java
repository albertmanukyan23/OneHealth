package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "doctorName", source = "doctor.name")
    PatientAppointmentDto map(Appointment appointment);

    List<PatientAppointmentDto> map(List<Appointment> appointments);
}
