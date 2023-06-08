package com.example.onehealth.entity;

import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends User {

    private String speciality;
    private String phoneNumber;
}
