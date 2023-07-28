package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthcommon.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PatientEndpointTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(User.builder()
                .id(1)
                .email("albertmanukyan@mail.com")
                .name("poxos")
                .surname("poxosyan")
                .password("poxosi")
                .token(null)
                .birthDate(new Date())
                .enabled(true)
                .userType(UserType.ADMIN).build());
    }
    @AfterEach
    void afterEach() {
        patientRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("test  valid patient registration")
    void register() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PatientRegisterDto patientRegisterDto = createValidPatientRegisterDto();
        mvc.perform(MockMvcRequestBuilders.post("/patients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRegisterDto)))
                .andExpect(status().isOk())
                .andExpect((MockMvcResultMatchers.jsonPath("$.id").exists()))
                .andExpect((MockMvcResultMatchers.jsonPath("$.email").value("test@example.com")));
    }

    @Test
    @DisplayName("test  valid failure patient registration")
    void registerWithUnValidPatient() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        PatientRegisterDto patientRegisterDto = new PatientRegisterDto();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/patients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientRegisterDto)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString, containsString("Name should not be empty"));
        assertThat(contentAsString, containsString("Email should not be empty"));
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void getPatientsTest() throws Exception {
        patientRepository
        mvc.perform(MockMvcRequestBuilders.get("/patients")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void getPatient() {
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void updatePatient() {
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void getPatientAppointments() {
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void removePatient() {
    }
    private PatientRegisterDto createValidPatientRegisterDto() {
        return PatientRegisterDto.builder()
                .name("test")
                .surname("test")
                .email("test@example.com")
                .password("password")
                .birthDate(new Date())
                .build();
    }
}