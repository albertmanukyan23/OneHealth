package com.example.onehealthcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRegisterDto {
    private String name;
    private String surname;
    private String email;
    private String password;
    private Date birthDate;
    private String region;
    private String address;
    private String nation;
}
