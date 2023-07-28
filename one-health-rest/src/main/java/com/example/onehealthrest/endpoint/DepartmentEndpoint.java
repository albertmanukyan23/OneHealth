package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.mapper.DepartmentMapper;
import com.example.onehealthrest.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
@Slf4j
public class DepartmentEndpoint {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping
    public ResponseEntity<DepartmentDto> addDepartment(@RequestBody
                                                       DepartmentDto departmentDto) {
        log.info(" method save() to add worked DepartmentEndpoint ");
        return ResponseEntity.ok(departmentService.save(departmentMapper.mapDto(departmentDto)));
    }

    @PutMapping("/update{id}")
    public ResponseEntity<DepartmentDto> updateDepartments(
            @RequestBody DepartmentDto departmentDto, @PathVariable("id") int id) {
        log.info(" method update() to update worked DepartmentEndpoint ");
        return ResponseEntity.ok(departmentService.update(departmentMapper.mapDto(departmentDto)));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartment() {
        List<DepartmentDto> departments = departmentService.departmentList()
                .stream()
                .map(departmentMapper::map)
                .toList();
        log.info(" method get() worked DepartmentEndpoint ");
        return ResponseEntity.ok(departments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        log.info(" method deleteById()  worked DepartmentEndpoint ");
        return departmentService.deleteById(id) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }
}
