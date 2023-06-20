package com.example.onehealth.service;
import com.example.onehealth.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    List<Doctor> getDoctors();

    Optional<Doctor>findDoctorById(int id);

    void update(Doctor doctor);
   Page<Doctor> getDoctorPage(Pageable pageable);
}
