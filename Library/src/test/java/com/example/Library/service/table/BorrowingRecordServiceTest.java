package com.example.Library.service.table;
import com.example.Library.classes.dto.BookDTO;
import com.example.Library.classes.dto.BorrowingRecordDTO;
import com.example.Library.classes.dto.PatronDTO;
import com.example.Library.classes.mapper.BookMapper;
import com.example.Library.classes.mapper.BorrowingRecordMapper;
import com.example.Library.classes.mapper.PatronMapper;
import com.example.Library.exception.DuplicateISBNException;
import com.example.Library.exception.ResourceNotFoundException;
import com.example.Library.model.table.Book;
import com.example.Library.model.table.BorrowingRecord;
import com.example.Library.model.table.Patron;
import com.example.Library.repository.table.BookRepository;
import com.example.Library.repository.table.BorrowingRecordRepository;
import com.example.Library.repository.table.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowingRecordServiceTest {

    @InjectMocks
    private BorrowingRecordService borrowingRecordService;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordMapper borrowingRecordMapper;

    private Book book;
    private Patron patron;
    private BorrowingRecord record;
    private BorrowingRecordDTO recordDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        patron = new Patron();
        patron.setId(1L);
        patron.setName("John Doe");

        record = new BorrowingRecord();
        record.setId(1L);
        record.setBook(book);
        record.setPatron(patron);
        record.setBorrowDate(LocalDate.now());

        recordDTO = new BorrowingRecordDTO();
        recordDTO.setId(1L);
    }

    @Test
    void returnBook_ShouldUpdateReturnDate_WhenRecordExists() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.of(record));
        when(borrowingRecordMapper.toDTO(any(BorrowingRecord.class))).thenReturn(recordDTO);

        BorrowingRecordDTO result = borrowingRecordService.returnBook(1L, 1L);

        assertNotNull(result);
        assertNotNull(record.getReturnDate());
        verify(borrowingRecordRepository).save(record);
    }

    @Test
    void returnBook_ShouldThrowException_WhenRecordNotFound() {
        when(borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDateIsNull(1L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowingRecordService.returnBook(1L, 1L));
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void borrowBook_ShouldCreateNewRecord_WhenBookAvailable() {
        when(borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(1L)).thenReturn(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(record);
        when(borrowingRecordMapper.toDTO(any(BorrowingRecord.class))).thenReturn(recordDTO);

        BorrowingRecordDTO result = borrowingRecordService.borrowBook(1L, 1L);

        assertNotNull(result);
        verify(borrowingRecordRepository).save(any(BorrowingRecord.class));
    }

    @Test
    void borrowBook_ShouldThrowException_WhenBookAlreadyBorrowed() {
        when(borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(1L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> borrowingRecordService.borrowBook(1L, 1L));
        verify(borrowingRecordRepository, never()).save(any());
    }

    @Test
    void getAllBorrowingRecords_ShouldReturnListOfRecords() {
        when(borrowingRecordRepository.findAll()).thenReturn(List.of(record));
        when(borrowingRecordMapper.toDTO(any(BorrowingRecord.class))).thenReturn(recordDTO);

        List<BorrowingRecordDTO> result = borrowingRecordService.getAllBorrowingRecords();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getBorrowingRecordById_ShouldReturnRecord_WhenExists() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        BorrowingRecord result = borrowingRecordService.getBorrowingRecordById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getBorrowingRecordById_ShouldThrowException_WhenNotFound() {
        when(borrowingRecordRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> borrowingRecordService.getBorrowingRecordById(1L));
    }
}
