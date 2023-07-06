package com.example.onehealth.entity;

import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends User {
    private String region;
    private String address;
    private String nation;

}
