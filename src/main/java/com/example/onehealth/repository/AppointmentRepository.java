package com.example.onehealth.repository;

import com.example.onehealth.entity.Appointment;
import com.example.onehealth.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    List<Appointment> findAllByDoctorId(int id);

    List<Appointment> findAllByPatientId(int id);

}