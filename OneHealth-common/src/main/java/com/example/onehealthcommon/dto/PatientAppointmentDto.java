package com.example.onehealthcommon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointmentDto {

    private String doctorName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
