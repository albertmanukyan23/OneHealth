package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.AppointmentDto;
import com.example.onehealthcommon.dto.CreateAppointmentDto;
import com.example.onehealthcommon.validation.ValidationChecker;
import com.example.onehealthrest.security.CurrentUser;
import com.example.onehealthrest.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentEndpoint {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAppointments(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "5") int size)
    {
        return ResponseEntity.ok(appointmentService.getAppointmentDtos(page - 1, size));
    }

    @PostMapping("/to-make")
    public ResponseEntity<?> makeAppointment(@RequestBody @Valid CreateAppointmentDto dto,
                                                          @AuthenticationPrincipal CurrentUser currentUser, BindingResult bindingResult)
    {
        StringBuilder validationResult = ValidationChecker.checkValidation(bindingResult);
        if (!validationResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult.toString());
        }
        Optional<AppointmentDto> optionalAppointmentDto = appointmentService.createAppointment(currentUser.getUser(), dto);
        return optionalAppointmentDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelAppointment(@RequestParam("id") int id,
                                               @AuthenticationPrincipal CurrentUser currentUser)
    {
        return appointmentService.cancelAppointmentById(id, currentUser.getUser()) ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}