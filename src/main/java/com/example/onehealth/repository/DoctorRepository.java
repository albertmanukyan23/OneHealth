package com.example.onehealth.repository;

import com.example.onehealth.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {


}
