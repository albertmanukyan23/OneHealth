package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.PatientRegisterDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.repository.PatientRepository;
import com.example.onehealthcommon.repository.UserRepository;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        userRepository.save(getUser());

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
    @DisplayName("getPatientsList() test")
    void getPatientsTest() throws Exception {
        savePatientsInDb();
        mvc.perform(MockMvcRequestBuilders.get("/patients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("patient@mail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value("patient2@mail.com"));

    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    @DisplayName("Get Patient By id")
    void getPatientTest() throws Exception {
        savePatientsInDb();
        int id = 3;
        mvc.perform(MockMvcRequestBuilders.get("/patients/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("patient2@mail.com"));

    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    @DisplayName("Get Patient By conflict id")
    void getPatientWithConflictIdTest() throws Exception {
        savePatientsInDb();
        int id = 8;
        mvc.perform(MockMvcRequestBuilders.get("/patients/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isConflict());
    }


    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    @DisplayName("update patient test")
    void updatePatientTest() throws Exception {
        savePatientsInDb();
        PatientRegisterDto patientRegisterDto = createValidPatientRegisterDto();
        int id = 3;
        String content = new ObjectMapper().writeValueAsString(patientRegisterDto);
        Optional<Patient> beforeRequestPatientOptional = patientRepository.findById(3);
        assertEquals("patient2@mail.com", beforeRequestPatientOptional.get().getEmail());
        mvc.perform(MockMvcRequestBuilders.put("/patients/update/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"));

        Optional<Patient> afterRequestPatientOptional = patientRepository.findById(3);
        assertEquals("test@example.com", afterRequestPatientOptional.get().getEmail());


    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    @DisplayName("update patient with errors test")
    void updatePatientWithErrorsTest() throws Exception {
        savePatientsInDb();
        PatientRegisterDto patientRegisterDto = new PatientRegisterDto();
        int id = 3;
        String content = new ObjectMapper().writeValueAsString(patientRegisterDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/patients/update/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString, containsString("Name should not be empty"));
        assertThat(contentAsString, containsString("Email should not be empty"));

    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    @DisplayName("Remove Patient Test")
    void removePatientTest() throws Exception {
        savePatientsInDb();
        int deletedPatientId = 3;
        Optional<Patient> beforeDeletePatientOptional = patientRepository.findById(deletedPatientId);
        assertTrue(beforeDeletePatientOptional.isPresent());
        mvc.perform(MockMvcRequestBuilders.delete("/patients/remove?id=" + deletedPatientId))
                .andExpect(status().is(204));
        assertTrue(patientRepository.findById(deletedPatientId).isEmpty());

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


    private User getUser() {
        return User.builder()
                .id(1)
                .email("albertmanukyan@mail.com")
                .name("poxos")
                .surname("poxosyan")
                .password("poxosi")
                .token(null)
                .birthDate(new Date())
                .enabled(true)
                .userType(UserType.ADMIN).build();
    }


    private void savePatientsInDb() {
        patientRepository.save(Patient.builder()
                .id(2)
                .name("patient1")
                .surname("patient1")
                .email("patient@mail.com")
                .password("12345678")
                .birthDate(new Date())
                .build());
        patientRepository.save(Patient.builder()
                .id(3)
                .name("patient2")
                .surname("patient2")
                .email("patient2@mail.com")
                .password("1234567810101")
                .birthDate(new Date())
                .build());
    }
}