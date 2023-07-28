package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.mapper.DepartmentMapper;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthrest.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto save(Department department) {
        Optional<Department> byId = departmentRepository.findById(department.getId());
        if (byId.isEmpty()) {
            departmentRepository.save(department);
            return departmentMapper.map(departmentRepository.save(department));
        }
        log.info("add method save() did not work ");
        return null;
    }

    @Override
    public DepartmentDto update(Department department) {
        Optional<Department> byId = departmentRepository.findById(department.getId());
        if (byId.isPresent()) {
            Department departmentDb = byId.get();
            departmentDb.setDepartments(departmentDb.getDepartments());
            departmentRepository.save(departmentDb);
            return departmentMapper.map(departmentDb);
        }
        log.info(" method update() did not work ");
        return null;
    }

    @Override
    public List<Department> departmentList() {
        return departmentRepository.findAll();
    }


    @Override
    public boolean deleteById(int id) {
        boolean isDeleted = false;
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            log.info(" method deleteById() did not work ");

            return true;
        }
        return isDeleted;
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
