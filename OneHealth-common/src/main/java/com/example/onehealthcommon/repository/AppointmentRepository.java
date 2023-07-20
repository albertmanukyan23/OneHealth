package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    List<Appointment> findAllByDoctorId(int id);

    List<Appointment> findAllByPatientId(int id);

}