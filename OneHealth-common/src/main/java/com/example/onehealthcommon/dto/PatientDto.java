package com.example.onehealthcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private int id;
    private String name;
    private String surname;
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private boolean enabled;
    private String picName;
    private String region;
    private String address;
    private String nation;
}
