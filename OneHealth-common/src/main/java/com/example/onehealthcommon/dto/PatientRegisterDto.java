package com.example.onehealthcommon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class PatientRegisterDto {

    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotBlank(message = "Surname should not be empty")
    private String surname;

    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password should not be empty")
    @Size(min = 5, message = "Password must have at least 5 characters")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Birthday Date should not be empty")
    private Date birthDate;

    private String region;
    private String address;
    private String nation;
}
