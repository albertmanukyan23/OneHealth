package com.example.onehealth.service;


import com.example.onehealth.entity.Department;
import com.example.onehealth.entity.User;

import java.util.List;

public interface DepartmentService {
    List<Department> getDepartment();

    void addDepartment(Department department);

    void update(Department department);
    void deleteDepartment(int id);
}
