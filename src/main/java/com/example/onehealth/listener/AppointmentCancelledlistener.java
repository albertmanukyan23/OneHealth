package com.example.onehealth.listener;

import com.example.onehealth.event.AppointmentCancelledEvent;
import com.example.onehealth.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentCancelledlistener implements ApplicationListener<AppointmentCancelledEvent> {
    private final EmailSenderService senderService;

    @Override
    public void onApplicationEvent(AppointmentCancelledEvent event) {
        String patientEmail = event.getPatientEmail();
        senderService.sendSimpleEmail(patientEmail, "Appointment was cancelled",
                "Your appointment has been canceled by the doctor," +
                " we ask for your forgiveness, try to book a consultation again for another day.");

    }
}
