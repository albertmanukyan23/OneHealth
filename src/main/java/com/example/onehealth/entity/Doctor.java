package com.example.onehealth.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Doctor extends User {

    private String speciality;
    private String phoneNumber;
}
