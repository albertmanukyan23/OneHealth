package com.example.onehealthrest.endpoint;

import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.entity.User;
import com.example.onehealthcommon.entity.UserType;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthcommon.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MedServEndpointTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MedServRepository medServRepository;

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
        medServRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getPriceListTest() throws Exception {
        MedServ medServ1 = new MedServ(1, "one", 1000);
        MedServ medServ2 = new MedServ(2, "two", 1000);
        MedServ medServ3 = new MedServ(3, "tree", 1000);
        medServRepository.saveAll(List.of(medServ1, medServ2, medServ3));
        mvc.perform(MockMvcRequestBuilders.get("/medical-services")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("one"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("two"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name").value("tree"));
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void createMedicalServiceTest() throws Exception {
        CreateMedServDto createMedServDto = CreateMedServDto.builder()
                .name("test")
                .price(1000)
                .build();
        String jsonDto = new ObjectMapper().writeValueAsString(createMedServDto);
        mvc.perform(MockMvcRequestBuilders.post("/medical-services/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1000));
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void updateMedicalServiceTest() throws Exception {
        MedServ medServ = MedServ.builder()
                .id(1)
                .name("test2")
                .price(500)
                .build();
        medServRepository.save(medServ);
        CreateMedServDto createMedServDto = CreateMedServDto.builder()
                .name("test")
                .price(1000)
                .build();
        String jsonDto = new ObjectMapper().writeValueAsString(createMedServDto);
        mvc.perform(MockMvcRequestBuilders.put("/medical-services/update/" + medServ.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(1000));
    }

    @Test
    @WithUserDetails("albertmanukyan@mail.com")
    void deleteMedicalServiceTest() throws Exception {
        MedServ medServ = medServRepository.save(MedServ.builder()
                .id(1)
                .name("test2")
                .price(500)
                .build());
        Optional<MedServ> byId = medServRepository.findById(medServ.getId());
        Assertions.assertTrue(byId.isPresent());
        mvc.perform(MockMvcRequestBuilders.delete("/medical-services/remove?id=" + medServ.getId()))
                .andExpect(status().is(204));
        Assertions.assertTrue(medServRepository.findById(medServ.getId()).isEmpty());

    }

}