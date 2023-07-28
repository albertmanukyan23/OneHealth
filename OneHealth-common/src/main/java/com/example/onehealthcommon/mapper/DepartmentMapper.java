package com.example.onehealthcommon.mapper;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.MedServ;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department mapDto(DepartmentDto departmentDto);
    DepartmentDto map(Department department);
    List<DepartmentDto> map(List<Department> departmentList);

}
