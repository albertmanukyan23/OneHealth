package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor mapDto(CreatDoctorRequestDto creatDoctorRequestDto);
    DoctorDtoResponse map(Doctor doctor);
}
