package com.example.onehealthcommon.repository;
import com.example.onehealthcommon.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> , QuerydslPredicateExecutor<Patient> {

    Optional<Patient> findByEmail(String email);
}
