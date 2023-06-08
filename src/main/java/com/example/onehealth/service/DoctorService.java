package com.example.onehealth.service;


import com.example.onehealth.entity.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {


    List<Doctor> getDoctors();

    Optional<Doctor>findDoctorById(int id);

    void update(Doctor doctor);
}
