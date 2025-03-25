package com.example.Library.service.table;


import com.example.Library.classes.dto.BookDTO;
import com.example.Library.classes.mapper.BookMapper;
import com.example.Library.exception.DuplicateISBNException;
import com.example.Library.exception.ResourceNotFoundException;
import com.example.Library.model.table.Book;
import com.example.Library.repository.table.BookRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    //    @Cacheable(value = "books")
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
    }


    //    @Cacheable(value = "books", key = "#id")
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));
        return bookMapper.toDTO(book);
    }

    //    @CacheEvict(value = "books", allEntries = true)
    @Transactional
    public BookDTO saveBook(BookDTO bookDTO) {
        try {
            if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
                throw new DuplicateISBNException("Book with ISBN " + bookDTO.getIsbn() + " already exists.");
            }
            Book book = bookMapper.toEntity(bookDTO);
            return bookMapper.toDTO(bookRepository.save(book));
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to save book: " + ex.getRootCause().getMessage());
        }
    }

    //    @CachePut(value = "books", key = "#id")
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + id + " not found"));

        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setIsbn(bookDTO.getIsbn());

        return bookMapper.toDTO(bookRepository.save(book));
    }

    //    @CacheEvict(value = "books", key = "#id")
    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patron with ID " + id + " not found"));

        try {
            bookRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Failed to delete patron due to database constraints.");
        }
    }

}