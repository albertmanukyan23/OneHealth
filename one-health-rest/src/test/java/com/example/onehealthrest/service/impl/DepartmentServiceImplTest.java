package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.DepartmentDto;
import com.example.onehealthcommon.entity.Department;
import com.example.onehealthcommon.exception.EntityNotFoundException;
import com.example.onehealthcommon.mapper.DepartmentMapper;
import com.example.onehealthcommon.repository.DepartmentRepository;
import com.example.onehealthrest.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {
    private final DepartmentRepository repository = Mockito.mock(DepartmentRepository.class);
    private final DepartmentMapper mapper = Mockito.mock(DepartmentMapper.class);

    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        departmentService = new DepartmentServiceImpl(repository, mapper);
    }

    @Test
    void save() {
        Department department = new Department(1, "depo");
        when(repository.findById(1)).thenReturn(Optional.of(department));
        departmentService.save(department);
        verify(repository, times(2)).save(department);

    }

    @Test
    void update() {
        Department department = new Department(1, "department");
        DepartmentDto departmentDto = new DepartmentDto(1, "department");
        when(repository.findById(departmentDto.getId())).thenReturn(Optional.of(department));
        when(mapper.map(department)).thenReturn(departmentDto);
        departmentService.update(departmentDto);
        verify(repository, times(1)).save(any());
    }

    @Test
    void departmentList() {
        Department department1 = new Department(1, "department1");
        Department department2 = new Department(2, "department2");
        List<Department> all = List.of(department1, department2);
        when(repository.findAll()).thenReturn(all);
        List<Department> departments = departmentService.departmentList();
        assertEquals(all, departments);

    }

    @Test
    void deleteById() {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> departmentService.deleteById(id));

    }
}