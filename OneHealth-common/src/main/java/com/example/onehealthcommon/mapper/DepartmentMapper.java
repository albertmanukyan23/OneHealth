package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department mapDto(DepartmentDto departmentDto);
    DepartmentDto map(Department department);
}
