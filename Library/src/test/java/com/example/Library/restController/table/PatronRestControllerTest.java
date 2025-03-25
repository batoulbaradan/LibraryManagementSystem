package com.example.Library.restController.table;

import com.example.Library.classes.dto.PatronDTO;
import com.example.Library.service.table.PatronService;
import com.example.Library.service.table.PatronService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PatronRestControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronRestController patronController;

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patronController).build();
    }

    @Test
    void testGetPatronById() throws Exception {
        PatronDTO patronDTO = new PatronDTO(1L, "John Doe", "john@example.com", "9987654567");

        when(patronService.getPatronById(1L)).thenReturn(patronDTO);

        mockMvc.perform(get("/api/patrons/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Patron retrieved successfully")))
                .andExpect(jsonPath("$.data.name", is("John Doe")))
                .andExpect(jsonPath("$.data.email", is("john@example.com")));
    }

    @Test
    void testGetAllPatrons() throws Exception {
        List<PatronDTO> patronsDTO = List.of(
                new PatronDTO(1L, "John Doe", "john@example.com", "9987654567"),
                new PatronDTO(2L, "Jane Doe", "jane@example.com", "9987654567")
        );

        when(patronService.getAllPatrons()).thenReturn(patronsDTO);

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Patrons retrieved successfully")))
                .andExpect(jsonPath("$.data[0].name", is("John Doe")))
                .andExpect(jsonPath("$.data[1].name", is("Jane Doe")));
    }

    @Test
    void testCreatePatron() throws Exception {
        PatronDTO patronDTO = new PatronDTO(null, "John Doe", "john@example.com", "9987654567");
        PatronDTO createdPatronDTO = new PatronDTO(1L, "John Doe", "john@example.com", "9987654567");

        when(patronService.savePatron(any(PatronDTO.class))).thenReturn(createdPatronDTO);

        mockMvc.perform(post("/api/patrons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patronDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Patron created successfully")))
                .andExpect(jsonPath("$.data.name", is("John Doe")));
    }

    @Test
    void testUpdatePatron() throws Exception {
        PatronDTO updatedPatronDTO = new PatronDTO(1L, "John Smith", "john.smith@example.com", "9987654567");

        when(patronService.updatePatron(eq(1L), any(PatronDTO.class))).thenReturn(updatedPatronDTO);

        mockMvc.perform(put("/api/patrons/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedPatronDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Patron updated successfully")))
                .andExpect(jsonPath("$.data.name", is("John Smith")))
                .andExpect(jsonPath("$.data.email", is("john.smith@example.com")));
    }

    @Test
    void testDeletePatron() throws Exception {
        doNothing().when(patronService).deletePatron(1L);

        mockMvc.perform(delete("/api/patrons/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Patron deleted successfully")));
    }
}
