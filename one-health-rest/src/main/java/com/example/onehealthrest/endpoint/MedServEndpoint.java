/*
*
*  //todo medical servieces description
*
* */
package com.example.onehealthrest.endpoint;


import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.mapper.MedServMapper;
import com.example.onehealthrest.service.MedServService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/medical-services")
public class MedServEndpoint {

    private final MedServService medServService;
    private final MedServMapper medServMapper;

    @GetMapping
    public ResponseEntity<List<MedServDto>> getPriceList()
    {
        List<MedServDto> priceList = medServService
                .getPriceList()
                .stream()
                .map(medServMapper::mapTo)
                .toList();
        return ResponseEntity.ok(priceList);
    }

    @PostMapping("/create")
    public ResponseEntity<MedServDto> createMedicalService(@RequestBody CreateMedServDto requestDto)
    {
        return ResponseEntity.ok(medServService.save(medServMapper.map(requestDto)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedServDto> updateMedicalService(@PathVariable("id") int id,
                                                           @RequestBody CreateMedServDto requestDto)
    {
        Optional<MedServDto> medServDtoOptional = medServService.update(id, requestDto);
        return medServDtoOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicalService(@PathVariable("id") int id)
    {
        log.info("deleteMedicalService() method inside MedServEndpoint has worked ");
        boolean delete = medServService.delete(id);
        return ResponseEntity.ok(delete);
    }
}