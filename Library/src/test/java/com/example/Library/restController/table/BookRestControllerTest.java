package com.example.Library.restController.table;

import com.example.Library.classes.dto.BookDTO;
import com.example.Library.service.table.BookService;
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
class BookRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookRestController bookRestController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookRestController).build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<BookDTO> books = Arrays.asList(
                new BookDTO(1L, "Book One", "Author One", 2021, "1234567890123"),
                new BookDTO(2L, "Book Two", "Author Two", 2022, "0987654321123")
        );

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Books retrieved successfully")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].title", is("Book One")))
                .andExpect(jsonPath("$.data[1].title", is("Book Two")));
    }

    @Test
    void testGetBookById() throws Exception {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO(bookId, "Book One", "Author One", 2021, "1234567890123");

        when(bookService.getBookById(bookId)).thenReturn(bookDTO);

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Book retrieved successfully")))
                .andExpect(jsonPath("$.data.title", is("Book One")));
    }

    @Test
    void testAddBook() throws Exception {
        BookDTO bookDTO = new BookDTO(null, "New Book", "New Author", 2023, "1122334455123");
        BookDTO savedBook = new BookDTO(1L, "New Book", "New Author", 2023, "1122334455123");

        when(bookService.saveBook(any(BookDTO.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Book added successfully")))
                .andExpect(jsonPath("$.data.id", is(1)));
    }

    @Test
    void testUpdateBook() throws Exception {
        Long bookId = 1L;
        BookDTO updatedBookDTO = new BookDTO(bookId, "Updated Book", "Updated Author", 2024, "5544332211123");

        when(bookService.updateBook(eq(bookId), any(BookDTO.class))).thenReturn(updatedBookDTO);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Book updated successfully")))
                .andExpect(jsonPath("$.data.title", is("Updated Book")));
    }

    @Test
    void testDeleteBook() throws Exception {
        Long bookId = 1L;

        doNothing().when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(bookId);
    }
}

