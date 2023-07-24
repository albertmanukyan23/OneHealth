package com.example.onehealthcommon.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private String name;
    private String surname;
    private String email;
    private Date birthDate;
    private boolean enabled;
    private String region;
    private String address;
    private String nation;
}
