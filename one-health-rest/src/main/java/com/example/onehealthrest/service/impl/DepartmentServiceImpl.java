package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.mapper.DepartmentMapper;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthrest.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;


    @Override
    public Optional<Department> findById(int id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Optional<Department> findByDepartments(String name) {
        return departmentRepository.findByDepartments(name);
    }

    @Override
    public Department save(Department department) {
        return departmentRepository.save(department);
        }


    @Override
    public List<DepartmentDto> fromDepartmentToDepartmentDto(List<Department> departments) {
        List<DepartmentDto> departmentDto = new ArrayList<>();
        for (Department dep : departments) {
            departmentDto.add(departmentMapper.map(dep));
        }
        return departmentDto;
    }


    @Override
    public boolean existsById(int id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public void deleteById(int id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
