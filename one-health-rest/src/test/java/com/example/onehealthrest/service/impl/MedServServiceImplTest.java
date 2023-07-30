package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.exception.EntityNotFoundException;
import com.example.onehealthcommon.mapper.MedServMapper;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthrest.service.MedServService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedServServiceImplTest {
    private final MedServRepository repository = Mockito.mock(MedServRepository.class);
    private final MedServMapper mapper = Mockito.mock(MedServMapper.class);

    private MedServService medServService;

    @BeforeEach
    void setUp() {
        medServService = new MedServServiceImpl(repository, mapper);
    }

    @Test
    @DisplayName("MedService Get PriceList Test")
    void getPriceListTest() {
        List<MedServ> expected = List.of(new MedServ(1, "service-one", 400),
                new MedServ(2, "service-two", 4000), new MedServ(3, "service-tree", 500));
        when(repository.findAll()).thenReturn(expected);
        List<MedServ> result = medServService.getPriceList();
        assertEquals(expected, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("MedService Get PriceList with exception Test")
    void getPriceListWithExceptionTest() {

        when(repository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EntityNotFoundException.class, ()-> medServService.getPriceList());
    }

    @Test
    @DisplayName("MedService Save Test")
    void medServiceSaveTest() {
        // given
        MedServ medServ = new MedServ(1, "service-one", 100);
        MedServDto medServDto = new MedServDto(1, "service-one", 100);
        // when
        when(repository.save(medServ)).thenReturn(medServ);
        when(mapper.mapTo(medServ)).thenReturn(medServDto);
        // act
        MedServDto result = medServService.save(medServ);
        // assert
        assertEquals(medServDto, result);
        verify(repository, times(1)).save(medServ);
    }

    @Test
    @DisplayName("Delete MedService With Id")
    void deleteMedServiceWithId() {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(new MedServ(1, "service-one", 100)));
        medServService.delete(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Delete MedService With Empty Optional")
    void deleteMedServiceWithEmptyOptional() {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> medServService.delete(id));


    }

    @Test
    @DisplayName("MedService find by id test")
    void findById() {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(new MedServ(1, "one-service", 400)));
        Optional<MedServ> byId = medServService.findById(id);
        verify(repository, times(1)).findById(id);
        assertTrue(byId.isPresent());

    }

    @Test
    @DisplayName("MedService find by id with empty optional  test")
    void findByIdWithEmptyOptional() {
        int id = 1;
        when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<MedServ> byId = medServService.findById(id);
        verify(repository, times(1)).findById(id);
        assertTrue(byId.isEmpty());
    }

    @Test
    @DisplayName("MedService update by id with optional  test")
    void updateMedServTest() {
        MedServ medServ = new MedServ(1, "one-service", 4000);
        CreateMedServDto createMedServDto = new CreateMedServDto("one-service", 4000);
        when(repository.findById(medServ.getId())).thenReturn(Optional.of(medServ));
        when(mapper.map(createMedServDto)).thenReturn(medServ);
        when(mapper.mapTo(medServ)).thenReturn(new MedServDto(1, "one-service", 4000));
        medServService.update(1, createMedServDto);
        verify(repository, times(1)).save(medServ);
    }

    @Test
    @DisplayName("MedService update by id with empty optional  test")
    void updateMedServWithEmptyOptionalTest() {
        int id = 1;
        CreateMedServDto createMedServDto = new CreateMedServDto("one-service", 4000);
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> medServService.update(id, createMedServDto));
    }
}