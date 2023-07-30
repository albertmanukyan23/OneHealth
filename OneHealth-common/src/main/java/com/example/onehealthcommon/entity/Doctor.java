package com.example.onehealthcommon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends User {

    @NotBlank(message = "Speciality should be indicated ")
    private String speciality;
    private String phoneNumber;
    private int zoomId;
    private String zoomPassword;
    @ManyToOne
    private Department department;


    public String doctorEmailSubject() {

        return "Hello,you have registered for an online consultation" +
                "You are registered" + this.getName() + "to the doctor";
    }

    public String getEmailBody(){
          return   "doctor data zoom:" + super.getPassword() + "password"
            + this.getZoomId() + "id";
    }

}
