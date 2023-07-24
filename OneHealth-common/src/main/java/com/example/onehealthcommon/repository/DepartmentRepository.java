package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartments(String name);

}