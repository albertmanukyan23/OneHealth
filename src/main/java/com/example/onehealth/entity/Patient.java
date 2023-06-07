package com.example.onehealth.entity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Patient extends User {

    private String region;
    private String address;
    private String nation;
}
