package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Optional<Department> findById(int id);

    Optional<Department> findByDepartments(String name);

    Department save(Department department);

    List<DepartmentDto> fromDepartmentToDepartmentDto(List<Department> departments);

    boolean existsById(int id);

    void deleteById(int id);

    List<Department> findAll();

}
