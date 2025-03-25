package com.example.Library.service.table;


import com.example.Library.classes.dto.BookDTO;
import com.example.Library.classes.dto.PatronDTO;
import com.example.Library.classes.mapper.BookMapper;
import com.example.Library.classes.mapper.PatronMapper;
import com.example.Library.exception.DuplicateISBNException;
import com.example.Library.exception.ResourceNotFoundException;
import com.example.Library.model.table.Book;
import com.example.Library.model.table.Patron;
import com.example.Library.repository.table.BookRepository;
import com.example.Library.repository.table.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private PatronMapper patronMapper;

    @InjectMocks
    private PatronService patronService;

    @Test
    void testGetAllPatrons() {
        List<Patron> patrons = List.of(new Patron(1L, "John Doe", "john@example.com", "123456789"));
        List<PatronDTO> patronDTOs = List.of(new PatronDTO(1L, "John Doe", "john@example.com", "123456789"));

        when(patronRepository.findAll()).thenReturn(patrons);
        when(patronMapper.toDTO(any())).thenReturn(patronDTOs.get(0));

        List<PatronDTO> result = patronService.getAllPatrons();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetPatronById() {
        Patron patron = new Patron(1L, "John Doe", "john@example.com", "123456789");
        PatronDTO patronDTO = new PatronDTO(1L, "John Doe", "john@example.com", "123456789");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(patronMapper.toDTO(any())).thenReturn(patronDTO);

        PatronDTO result = patronService.getPatronById(1L);

        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
    }

    @Test
    void testSavePatron() {
        PatronDTO patronDTO = new PatronDTO(null, "John Doe", "john@example.com", "123456789");
        Patron patron = new Patron(1L, "John Doe", "john@example.com", "123456789");

        when(patronMapper.toEntity(any())).thenReturn(patron);
        when(patronRepository.save(any())).thenReturn(patron);
        when(patronMapper.toDTO(any())).thenReturn(new PatronDTO(1L, "John Doe", "john@example.com", "123456789"));

        PatronDTO savedPatron = patronService.savePatron(patronDTO);

        assertNotNull(savedPatron.getId());
        assertEquals("John Doe", savedPatron.getName());
    }

    @Test
    void testUpdatePatron() {
        PatronDTO updatedPatronDTO = new PatronDTO(null, "John Smith", "john.smith@example.com", "987654321");
        Patron existingPatron = new Patron(1L, "John Doe", "john@example.com", "123456789");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(existingPatron));
        when(patronRepository.save(any())).thenReturn(existingPatron);
        when(patronMapper.toDTO(any())).thenReturn(new PatronDTO(1L, "John Smith", "john.smith@example.com", "987654321"));

        PatronDTO result = patronService.updatePatron(1L, updatedPatronDTO);

        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());
    }

    @Test
    void testDeletePatron() {
        Patron patron = new Patron(1L, "John Doe", "john@example.com", "123456789");

        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        doNothing().when(patronRepository).delete(patron);

        assertDoesNotThrow(() -> patronService.deletePatron(1L));
    }
}
