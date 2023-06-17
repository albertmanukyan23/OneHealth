package com.example.onehealth.event;

import org.springframework.context.ApplicationEvent;

public class DoctorRegistrationEvent extends ApplicationEvent {
    private final String doctorEmail;
    private final String password;

    public DoctorRegistrationEvent(Object source, String doctorEmail, String password) {
        super(source);
        this.doctorEmail = doctorEmail;
        this.password = password;
    }
    public String getDoctorEmail() {
        return doctorEmail;
    }

    public String getPassword() {
        return password;
    }
}
