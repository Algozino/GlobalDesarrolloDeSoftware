package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class) // Especifica qué controlador probar
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // simula MutantService para que Spring pueda inyectarlo
    @MockBean
    private MutantService mutantService;

    // simula StatsService para que Spring pueda inyectarlo
    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK si es Mutante")
    void testMutantEndpoint_ReturnOk() throws Exception {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);
        when(mutantService.analyzeDna(any())).thenReturn(true); // Simular que es mutante

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()); // 200 OK
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden si es Humano")
    void testHumanEndpoint_ReturnForbidden() throws Exception {
        // Arrange
        String[] dna = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
        DnaRequest request = new DnaRequest(dna);
        when(mutantService.analyzeDna(any())).thenReturn(false); // Simular que es humano

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // 403 Forbidden
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request si el ADN es inválido")
    void testInvalidDna_ReturnBadRequest() throws Exception {
        // Arrange
        String[] dna = {"ATGX", "CAGT"}; // Inválido (No es NxN y tiene 'X')
        DnaRequest request = new DnaRequest(dna);

        // Act & Assert
        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK y las estadísticas")
    void testStatsEndpoint_ReturnOk() throws Exception {
        // Arrange
        StatsResponse stats = new StatsResponse(40, 100, 0.4);
        when(statsService.getStats()).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").value(40))
                .andExpect(jsonPath("$.count_human_dna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }
}