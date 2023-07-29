package com.example.onehealthmvc.service.impl;

import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthmvc.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public Page<Department> getDepartment(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public List<Department> getDepartmentList() {
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

    //Retrieves a paginated list of Department objects based on the page number and size
    @Override
    public Page<Department> getDepartmentPageData(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return departmentRepository.findAll(pageable);
    }

    //Generates a list of page numbers for paginated results.
    @Override
    public List<Integer> getPageNumbers(int totalPages) {
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return null;
    }
}