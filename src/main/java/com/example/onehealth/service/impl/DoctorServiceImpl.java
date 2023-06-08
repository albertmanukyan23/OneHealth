package com.example.onehealth.service.impl;
import com.example.onehealth.entity.Doctor;
import com.example.onehealth.repository.DoctorRepository;
import com.example.onehealth.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    @Override
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }
}
