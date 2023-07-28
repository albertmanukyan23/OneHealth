package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.mapper.DepartmentMapper;
import com.example.onehealthrest.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentEndpoint {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;

    @PostMapping()
    public ResponseEntity<DepartmentDto> addDepartment(@RequestBody DepartmentDto departmentDto) {
        Optional<Department> byId = departmentService.findById(departmentDto.getId());
        if (byId.isEmpty()) {
            Department department = departmentMapper.mapDto(departmentDto);
            departmentService.save(department);
            return ResponseEntity.ok(departmentMapper.map(department));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping()
    public ResponseEntity<DepartmentDto> updateDepartments(
            @RequestBody DepartmentDto departmentDto) {
        Optional<Department> byId = departmentService.findById(departmentDto.getId());
        if (byId.isPresent()) {
            Department department = byId.get();
            department.setDepartments(departmentDto.getDepartments());
            return ResponseEntity.ok(departmentMapper.map(departmentService.save(department)));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<Department> all = departmentService.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<DepartmentDto> departmentDto = departmentService.fromDepartmentToDepartmentDto(all);
        return ResponseEntity.ok(departmentDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        if (departmentService.existsById(id)) {
            departmentService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
