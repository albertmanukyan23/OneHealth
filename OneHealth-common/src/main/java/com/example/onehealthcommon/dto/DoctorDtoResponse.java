package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.UserType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDtoResponse {
    private int id;
    private String name;
    private String surname;
    private String email;
    private Date birthDate;
  //  private String picName;
    private String speciality;
    private String phoneNumber;
    @ManyToOne
    private Department department;
    private int zoomId;
    private String zoomPassword;
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
