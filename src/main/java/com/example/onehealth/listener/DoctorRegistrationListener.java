package com.example.onehealth.listener;

import com.example.onehealth.event.DoctorRegistrationEvent;
import com.example.onehealth.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorRegistrationListener implements ApplicationListener<DoctorRegistrationEvent> {
    private final EmailSenderService senderService;

    @Override
    public void onApplicationEvent(DoctorRegistrationEvent event) {
        String email = event.getDoctorEmail();
        String password = event.getPassword();
        senderService.sendSimpleEmail(email, "You password for Log in OneHealth",
                "password: " + password +"\n Please don't lose it.");
    }
}
