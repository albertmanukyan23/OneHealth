package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.RegisterType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAppointmentDto {

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "startTime Date should not be empty")
    private LocalDateTime startTime;

    @Positive(message = "Field should be greater than zero")
    private int doctorId;
    @Positive(message = "Field should be greater than zero")
    private int patientId;
    private int departmentId;

    @Enumerated(EnumType.STRING)
    private RegisterType registerType;
}
