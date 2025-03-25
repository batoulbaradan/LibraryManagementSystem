package com.example.Library.restController.table;

import com.example.Library.classes.dto.BookDTO;
import com.example.Library.classes.dto.BorrowingRecordDTO;
import com.example.Library.service.table.BookService;
import com.example.Library.service.table.BorrowingRecordService;
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
public class BorrowingRecordRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BorrowingRecordService borrowingRecordService; // Now injected via @MockImport

    @InjectMocks
    private BorrowingRecordRestController borrowingRecordRestController;
    private BorrowingRecordDTO recordDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(borrowingRecordRestController).build();
        recordDTO = new BorrowingRecordDTO();
        recordDTO.setId(1L);
    }

    @Test
    void getAllBorrowingRecords_ShouldReturnListOfRecords() throws Exception {
        List<BorrowingRecordDTO> records = List.of(recordDTO);
        when(borrowingRecordService.getAllBorrowingRecords()).thenReturn(records);

        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Borrowing records retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void borrowBook_ShouldReturnCreatedRecord() throws Exception {
        when(borrowingRecordService.borrowBook(1L, 1L)).thenReturn(recordDTO);

        mockMvc.perform(post("/api/borrow/1/patron/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book borrowed successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void returnBook_ShouldReturnUpdatedRecord() throws Exception {
        when(borrowingRecordService.returnBook(1L, 1L)).thenReturn(recordDTO);

        mockMvc.perform(put("/api/return/1/patron/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book returned successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }
}
