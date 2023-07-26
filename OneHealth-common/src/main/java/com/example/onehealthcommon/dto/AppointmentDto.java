package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.RegisterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

    private int id;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private DoctorDtoResponse doctorDto;
    private PatientDto patientDto;
    private DepartmentDto departmentDto;
    private RegisterType registerType;
}
