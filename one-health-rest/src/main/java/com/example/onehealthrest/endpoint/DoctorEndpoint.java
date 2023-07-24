package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.dto.UpdateDoctor;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.mapper.DoctorMapper;
import com.example.onehealthcommon.repository.UserRepository;
import com.example.onehealthrest.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorEndpoint {
    private final DoctorService doctorService;
    private final PasswordEncoder passwordEncoder;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody CreatDoctorRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorBuilder = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorBuilder.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBuilder.toString());
        }
        Optional<Doctor> byEmail = doctorService.findByEmail(requestDto.getEmail());
        if (byEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.ok(doctorService.save(doctorMapper.mapDto(requestDto)));
    }

//    @PutMapping
//    public ResponseEntity<DoctorDtoResponse> modify(@RequestBody UpdateDoctor updateDoctor,
//                                               @RequestParam("id") int id) {
//        Optional<Doctor> byId = doctorService.findById(id);
//        if (byId.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        Doctor doctor = byId.get();
//        Doctor updateDoc = doctorService.update(doctor, updateDoctor);
//        DoctorDtoResponse doctorDto = doctorMapper.map(doctorService.save(updateDoc));
//        return doctorDto;
//    }
//    }
}