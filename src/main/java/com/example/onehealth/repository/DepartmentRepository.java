package com.example.onehealth.repository;

import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}