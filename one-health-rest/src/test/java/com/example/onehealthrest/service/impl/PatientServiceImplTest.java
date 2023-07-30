//package com.example.onehealthrest.service.impl;
//
//import com.example.onehealthcommon.dto.PatientDto;
//import com.example.onehealthcommon.dto.PatientRegisterDto;
//import com.example.onehealthcommon.entity.Patient;
//import com.example.onehealthcommon.mapper.PatientMapper;
//import com.example.onehealthcommon.repository.PatientRepository;
//import com.example.onehealthrest.service.PatientService;
//import com.example.onehealthrest.service.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PatientServiceImplTest {
//
//    private PatientService patientService;
//
//    private final PatientRepository patientRepository = Mockito.mock(PatientRepository.class);
//    private final PatientMapper mapper = Mockito.mock(PatientMapper.class);
//    private final UserService userService = Mockito.mock(UserService.class);
//    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
//
//    @BeforeEach
//    void setUp() {
//        patientService = new PatientServiceImpl(patientRepository, userService, mapper, passwordEncoder);
//    }
//
//    @Test
//    @DisplayName("Patient saving test")
//    void savePatientTest() {
//        //given
//        Patient patient = getPatient();
//
//        PatientDto expected = PatientDto.builder()
//                .id(1)
//                .name("patient")
//                .surname("patient")
//                .email("patient@mail.com")
//                .address("Gyumri")
//                .birthDate(patient.getBirthDate())
//                .enabled(true)
//                .nation("armenian")
//                .build();
//        //when
//        when(mapper.map(patient)).thenReturn(expected);
//        //act
//        PatientDto result = patientService.save(patient);
//        verify(userService, times(1)).registerUser(patient);
//        Assertions.assertEquals(expected, result);
//    }
//
//    @Test
//    @DisplayName("Test getting patients dto list ")
//    void getPatientsDtoListTest() {
//        int size = 5;
//        int page = 1;
//        List<Patient> patientList = List.of(getPatient());
//        PatientDto patientDto = PatientDto.builder()
//                .id(1)
//                .name("patient")
//                .surname("patient")
//                .email("patient@mail.com")
//                .address("Gyumri")
//                .birthDate(new Date())
//                .enabled(true)
//                .nation("armenian").build();
//        List<PatientDto> patientDtoList = List.of(patientDto);
//        PageRequest pageRequest = PageRequest.of(page, size);
//        //mocking static fields
//        mockStatic(PageRequest.class);
//        Page<Patient> mockPatientPage = new PageImpl<>(patientList, pageRequest, patientList.size());
//        Mockito.when(PageRequest.of(page, size)).thenReturn(pageRequest);
//        //when
//        when(patientRepository.findAll(pageRequest)).thenReturn(mockPatientPage);
//        when(mapper.mapListToDtos(any())).thenReturn(patientDtoList);
//        patientService.getPatientsDtoList(page, size);
//        //act
//        verify(patientRepository, times(1)).findAll(pageRequest);
//        Assertions.assertEquals(patientList, mockPatientPage.getContent());
//
//    }
//
//
//    @Test
//    @DisplayName("Patient updating test")
//    void updatePatientTest() {
//        int id = 1;
//        Patient patient = getPatient();
//        PatientRegisterDto patientRegisterDto = getPatientRegisterDto();
//        //when
//        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
//        when(patientRepository.findByEmail(patientRegisterDto.getEmail())).thenReturn(Optional.empty());
//        when(patientRepository.save(any())).thenReturn(patient);
//        //act
//        patientService.update(patientRegisterDto, id);
//        verify(patientRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("Patient update by id with empty optional  test")
//    void updatePatientByEmptyOptional() {
//        int id = 1;
//        PatientRegisterDto patientRegisterDto = getPatientRegisterDto();
//        when(patientRepository.findById(id)).thenReturn(Optional.empty());
//        patientService.update(patientRegisterDto, id);
//        verify(patientRepository, times(0)).save(any());
//    }
//
//    @Test
//    @DisplayName("Delete Patient With Empty Optional")
//    void deletePatientWithEmptyOptionalTest() {
//        int id = 1;
//        when(patientRepository.findById(id)).thenReturn(Optional.empty());
//        patientService.delete(id);
//        verify(patientRepository, times(0)).deleteById(id);
//
//    }
//
//    @Test
//    @DisplayName("Delete Patient With Optional")
//    void deletePatientWithOptionalTest() {
//        int id = 1;
//        when(patientRepository.findById(id)).thenReturn(Optional.of(getPatient()));
//        patientService.delete(id);
//        verify(patientRepository, times(1)).deleteById(id);
//
//    }
//
//    @Test
//    @DisplayName("Find Patient by id")
//    void findPatientByIdTest() {
//        int id = 1;
//        when(patientRepository.findById(id)).thenReturn(Optional.of(getPatient()));
//        Optional<Patient> patientById = patientService.findPatientById(id);
//        verify(patientRepository, times(1)).findById(id);
//        Assertions.assertTrue(patientById.isPresent());
//
//    }
//
//
//    private Patient getPatient() {
//        return Patient.builder()
//                .id(1)
//                .name("patient")
//                .surname("patient")
//                .email("patient@mail.com")
//                .password("secret-password")
//                .address("Gyumri")
//                .birthDate(new Date())
//                .enabled(true)
//                .token(null)
//                .nation("armenian").build();
//
//    }
//
//    private PatientRegisterDto getPatientRegisterDto() {
//        return PatientRegisterDto.builder()
//                .name("patient")
//                .surname("patient")
//                .email("patient@mail.com")
//                .password("secret-password")
//                .address("Gyumri")
//                .birthDate(new Date())
//                .nation("armenian")
//                .build();
//    }
//
//}