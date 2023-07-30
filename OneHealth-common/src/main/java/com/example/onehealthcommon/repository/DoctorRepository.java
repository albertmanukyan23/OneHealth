package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>,
        QuerydslPredicateExecutor<Doctor> {
    Optional<Doctor> findByEmail(String email);

    List<Doctor> findBySurnameContaining(String key);

}
