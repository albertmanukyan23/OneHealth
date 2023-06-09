package com.example.onehealth.repository;

import com.example.onehealth.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Optional<Doctor> findByEmail(String email);
    List<Doctor> findBySurnameContaining(String key);

}
