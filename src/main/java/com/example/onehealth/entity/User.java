package com.example.onehealth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regisDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Birthday Date should not be empty")
    private Date birthDate;
    private String picName;
    @Enumerated(value = EnumType.STRING)
    private UserType userType;

}
