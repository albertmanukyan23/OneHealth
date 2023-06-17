package com.example.onehealth.event;

import org.springframework.context.ApplicationEvent;

public class AppointmentCancelledEvent extends ApplicationEvent {
    private final String patientEmail;
    public AppointmentCancelledEvent(Object source, String patientEmail) {
        super(source);
        this.patientEmail = patientEmail;
    }
    public String getPatientEmail(){
        return patientEmail;
    }
}
