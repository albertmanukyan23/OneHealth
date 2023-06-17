package com.example.onehealth.service.impl;
import com.example.onehealth.entity.Department;
import com.example.onehealth.repository.DepartmentRepository;
import com.example.onehealth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    @Override
    public List<Department> getDepartment() {
        return departmentRepository.findAll();
    }

    @Override
    public void addDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void update(Department department) {
        Optional<Department> byId = departmentRepository.findById(department.getId());
        if (byId.isPresent()) {
            Department departmentDb = byId.get();
            departmentDb.setDepartments(department.getDepartments());
            departmentRepository.save(departmentDb);
        }
    }

    @Override
    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }
}
