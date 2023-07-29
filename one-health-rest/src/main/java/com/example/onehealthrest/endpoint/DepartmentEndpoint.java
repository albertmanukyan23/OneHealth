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
import java.util.Optional;

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
        return ResponseEntity.ok(departmentService.save(departmentMapper.mapDto(departmentDto)));
    }

    @PutMapping("/update")
    public ResponseEntity<Optional<DepartmentDto>> updateDepartments(
            @RequestBody DepartmentDto departmentDto) {
       return ResponseEntity.ok(departmentService.update(departmentDto));
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
        boolean delete = departmentService.deleteById(id);
        return ResponseEntity.ok(delete);
    }
}
