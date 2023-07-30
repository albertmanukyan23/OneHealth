package com.example.onehealthcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientSearchDto {

    private String name;
    private String surname;
    private String email;
    private String region;
    private String address;
    private String nation;
    private Date birthdayFrom;
    private Date birthdayTo;
    private String sortBy;
    private String sortDirection;
}
