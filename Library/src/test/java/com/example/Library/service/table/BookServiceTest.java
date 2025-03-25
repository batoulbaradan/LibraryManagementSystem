package com.example.Library.service.table;


import com.example.Library.classes.dto.BookDTO;
import com.example.Library.classes.mapper.BookMapper;
import com.example.Library.exception.DuplicateISBNException;
import com.example.Library.exception.ResourceNotFoundException;
import com.example.Library.model.table.Book;
import com.example.Library.repository.table.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Enables Mockito in JUnit 5
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Mockito Guide");
        book.setAuthor("John Doe");
        book.setIsbn("123-456-78912");
        book.setPublicationYear(2021);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Mockito Guide");
        bookDTO.setAuthor("John Doe");
        bookDTO.setIsbn("123-456-78921");
        book.setPublicationYear(2023);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBookDTOs() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book));
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mockito Guide", result.get(0).getTitle());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_ShouldReturnBookDTO_WhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Mockito Guide", result.getTitle());

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(1L));

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void saveBook_ShouldReturnBookDTO_WhenValid() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.saveBook(bookDTO);

        assertNotNull(result);
        assertEquals("Mockito Guide", result.getTitle());

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void saveBook_ShouldThrowException_WhenDuplicateIsbn() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        assertThrows(DuplicateISBNException.class, () -> bookService.saveBook(bookDTO));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldReturnUpdatedBookDTO_WhenValid() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO result = bookService.updateBook(1L, bookDTO);

        assertNotNull(result);
        assertEquals("Mockito Guide", result.getTitle());

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(1L, bookDTO));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldDeleteBook_WhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_ShouldThrowException_WhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));

        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteBook_ShouldThrowDataIntegrityViolationException_WhenDatabaseIssue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doThrow(new DataIntegrityViolationException("Database constraint")).when(bookRepository).deleteById(1L);

        assertThrows(DataIntegrityViolationException.class, () -> bookService.deleteBook(1L));

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
