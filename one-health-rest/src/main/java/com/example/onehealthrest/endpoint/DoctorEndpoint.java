package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;

import com.example.onehealthcommon.dto.DoctorSearchDto;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.mapper.DoctorMapper;
import com.example.onehealthcommon.validation.ValidationChecker;
import com.example.onehealthrest.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/doctors")
@Slf4j
public class DoctorEndpoint {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid CreatDoctorRequestDto requestDto, BindingResult bindingResult) {
        StringBuilder stringBuilder = ValidationChecker.checkValidation(bindingResult);
        if (!stringBuilder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(stringBuilder.toString());
        }
        Optional<Doctor> byEmail = doctorService.findByEmail(requestDto.getEmail());
        return byEmail.isPresent() ? ResponseEntity.status(HttpStatus.CONFLICT).build() :
                ResponseEntity.ok(doctorService.save(doctorMapper.mapDto(requestDto)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> modify(@RequestBody @Valid CreatDoctorRequestDto creatDoctorRequestDto,
                                    @PathVariable("id") int id, BindingResult bindingResult) {
        StringBuilder stringBuilder = ValidationChecker.checkValidation(bindingResult);
        if (!stringBuilder.isEmpty()) {
            return ResponseEntity.
                    status(HttpStatus.BAD_REQUEST).body(stringBuilder.toString());
        }
        Optional<Doctor> update = doctorService.update(creatDoctorRequestDto, id);
        return update.map(ResponseEntity::ok).
                orElseGet(() -> ResponseEntity.
                        status(HttpStatus.CONFLICT).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable("id") int id) {
        Optional<Doctor> doctorById = doctorService.getDoctorById(id);
        return doctorById.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.
                        status(HttpStatus.CONFLICT).build());
    }

    @PostMapping("/search")
    public ResponseEntity<List<DoctorDtoResponse>> getDoctorList(@RequestParam(defaultValue = "5") int size,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestBody DoctorSearchDto doctorSearchDto)
    {
        return ResponseEntity.ok(doctorService.searchDoctor(size, page - 1, doctorSearchDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable("id") int id) {
        boolean delete = doctorService.deleteById(id);
        return ResponseEntity.ok(delete);
    }
}
