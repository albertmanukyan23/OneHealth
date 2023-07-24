package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.mapper.PatientMapper;
import com.example.onehealthrest.service.AppointmentService;
import com.example.onehealthrest.service.PatientService;
import com.example.onehealthrest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientEndpoint {

    private final UserService userService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final PatientMapper patientMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody PatientRegisterDto patientRegisterDto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            StringBuilder errorBuilder = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorBuilder.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBuilder.toString());
        }
        return ResponseEntity.ok(patientService.save(patientMapper.map(patientRegisterDto)));

    }
}
