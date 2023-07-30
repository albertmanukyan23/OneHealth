package com.example.onehealthrest.service.impl;
import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.exception.EntityConflictException;
import com.example.onehealthcommon.exception.EntityNotFoundException;
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
            throw new EntityConflictException("Department with " + department.getId() + " there exist");
        } else {
            departmentRepository.save(department);
            return departmentMapper.map(departmentRepository.save(department));
        }
    }

    @Override
    public Optional<DepartmentDto> update(DepartmentDto departmentDto) {
        Optional<Department> byId = departmentRepository.findById(departmentDto.getId());
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("Department with " + departmentDto.getId() + " does not exist");
        } else {
            Department department = byId.get();
            department.setDepartments(departmentDto.getDepartments());
            departmentRepository.save(department);
            return Optional.of(departmentMapper.map(department));

        }

    }

    @Override
    public List<Department> departmentList() {
        List<Department> all = departmentRepository.findAll();
        if (all.isEmpty()) {
            throw new EntityNotFoundException("Is Empty " + all);
        } else {
            return all;
        }
    }


    @Override
    public boolean deleteById(int id) {
        Optional<Department> byId = departmentRepository.findById(id);
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("DeleteById with " + id + " does not exist");
        } else {
            departmentRepository.deleteById(id);
            return true;
        }
    }

}
