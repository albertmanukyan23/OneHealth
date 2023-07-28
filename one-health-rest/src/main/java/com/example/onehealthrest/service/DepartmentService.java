package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentDto save(Department department);
    DepartmentDto update(Department department);

    List<Department> departmentList();
    boolean deleteById(int id);

    List<Department> findAll();

}
