package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.CreatDoctorRequestDto;
import com.example.onehealthcommon.dto.DoctorDtoResponse;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.mapper.DoctorMapper;
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
        log.info("for the doctor,the method for registration worked");
        StringBuilder stringBuilder = doctorService.checkValidation(bindingResult);
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
        log.info("for the doctor,the method worked for a change");
        StringBuilder stringBuilder = doctorService.checkValidation(bindingResult);
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
        log.info("see a doctor getDoctorId() method worked");
        Optional<Doctor> doctorById = doctorService.getDoctorById(id);
        return doctorById.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.
                        status(HttpStatus.CONFLICT).build());
    }

    @GetMapping()
    public ResponseEntity<List<DoctorDtoResponse>> getDoctorList(@RequestParam(defaultValue = "5") int size,
                                                                 @RequestParam(defaultValue = "1") int page) {
        log.info("see doctor getDoctor() method worked ");
        return ResponseEntity.ok(doctorService.getDoctorList(size, page - 1));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> deleteDoctor(@RequestParam("id") int id) {
        log.info("deleteById() doctor method worked");
        return doctorService.deleteById(id) ? ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
