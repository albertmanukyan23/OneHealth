package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.PatientAppointmentDto;
import com.example.onehealthcommon.dto.PatientDto;
import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.dto.UserPageDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.mapper.PatientMapper;
import com.example.onehealthcommon.mapper.UserMapper;
import com.example.onehealthrest.security.CurrentUser;
import com.example.onehealthrest.service.AppointmentService;
import com.example.onehealthrest.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientEndpoint {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final PatientMapper patientMapper;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid PatientRegisterDto patientRegisterDto, BindingResult bindingResult) throws IOException {
        StringBuilder stringBuilder = patientService.checkValidation(bindingResult);
        if (!stringBuilder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringBuilder.toString());
        }
        return ResponseEntity.ok(patientService.save(patientMapper.map(patientRegisterDto)));

    }

    @GetMapping()
    public ResponseEntity<List<PatientDto>> getPatients(@RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(patientService.getPatientsDtoList(page - 1, size));
    }

    @GetMapping("/single-page")
    public ResponseEntity<UserPageDto> singlePage(@AuthenticationPrincipal CurrentUser currentUser) {

        return ResponseEntity.ok(userMapper.mapToUserPageDto(currentUser.getUser()));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatient(@PathVariable("id") int id) {
        Optional<Patient> patientById = patientService.findPatientById(id);
        return patientById.map(patient -> ResponseEntity.ok(patientMapper.map(patient))).orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable("id") int id,
                                           @RequestBody @Valid PatientRegisterDto patientRegisterDto,
                                           BindingResult bindingResult) {
        StringBuilder stringBuilder = patientService.checkValidation(bindingResult);
        if (!stringBuilder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringBuilder.toString());
        }
        Optional<Patient> update = patientService.update(patientRegisterDto, id);
        return update.map(patient -> ResponseEntity.ok(patientMapper.map(patient))).orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());

    }

    @GetMapping("/appointments")
    public ResponseEntity<List<PatientAppointmentDto>> getPatientAppointments(@AuthenticationPrincipal CurrentUser currentUser) {

        return ResponseEntity.ok(appointmentService.getPatientAppointments(currentUser.getUser()));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removePatient(@RequestParam("id") int id) {

        return patientService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
