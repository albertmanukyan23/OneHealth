package com.example.onehealth.service;
import com.example.onehealth.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    Page<Department> getDepartment(Pageable pageable);

    List<Department> getDepartmentList();

    void addDepartment(Department department);

    void update(Department department);
    void deleteDepartment(int id);

}
