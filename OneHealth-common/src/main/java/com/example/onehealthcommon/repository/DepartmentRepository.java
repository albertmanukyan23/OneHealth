package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}