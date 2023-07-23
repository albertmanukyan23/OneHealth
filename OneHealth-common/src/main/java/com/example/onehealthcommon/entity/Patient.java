package com.example.onehealthcommon.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User {

    private String region;
    private String address;
    private String nation;
//    @Builder // Use @Builder here to generate the builder for Patient
//    public Patient(String name, String surname, String email, String password, Date birthDate, String region, String address, String nation) {
//        super(name, surname, email, password,birthDate); // Call the superclass constructor
//        this.region = region;
//        this.address = address;
//        this.nation = nation;
//    }

}
