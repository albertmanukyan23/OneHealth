package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    List<Doctor> getDoctors();

    Optional<Doctor> findDoctorById(int id);

    void update(Doctor doctor, MultipartFile multipartFile) ;

    Page<Doctor> getDoctorPage(Pageable pageable);


    void registerDoctor(Doctor doctor, MultipartFile multipartFile) ;

    Page<Doctor> getDoctorPageData(Optional<Integer> page, Optional<Integer> size);

    List<Integer> getNumbersPage(int totalPages);

    List<Doctor> searchDoctorsByKey(String searchText);

}
