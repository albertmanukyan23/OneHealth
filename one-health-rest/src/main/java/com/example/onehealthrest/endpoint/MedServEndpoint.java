package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.mapper.MedServMapper;
import com.example.onehealthrest.service.MedServService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medical-services")
public class MedServEndpoint {

    private final MedServService medServService;
    private final MedServMapper medServMapper;

    @GetMapping
    public ResponseEntity<List<MedServDto>> getPriceList() {
        List<MedServDto> priceList = medServService.getPriceList().stream().map(medServMapper::map).toList();
        return ResponseEntity.ok(priceList);
    }

    @PostMapping("/create")
    public ResponseEntity<MedServ> createMedicalService(@RequestBody MedServDto requestDto) {
        return ResponseEntity.ok(medServService.save(medServMapper.map(requestDto)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MedServ> updateMedicalService(@PathVariable("id") int id, @RequestBody MedServDto requestDto) {
        MedServ medServ = medServService.update(id, requestDto);
        if (medServ != null) {
            return ResponseEntity.ok(medServService.save(medServ));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> deleteMedicalService(@RequestParam("id") int id) {
        if (medServService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}