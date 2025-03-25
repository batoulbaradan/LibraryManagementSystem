package com.example.Library.service.table;


import com.example.Library.classes.dto.BorrowingRecordDTO;
import com.example.Library.classes.mapper.BorrowingRecordMapper;
import com.example.Library.exception.ResourceNotFoundException;
import com.example.Library.model.table.Book;
import com.example.Library.model.table.BorrowingRecord;
import com.example.Library.model.table.Patron;
import com.example.Library.repository.table.BookRepository;
import com.example.Library.repository.table.BorrowingRecordRepository;
import com.example.Library.repository.table.PatronRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;import org.springframework.transaction.annotation.Transactional;


@Service
public class BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;


    private final BookRepository bookRepository;


    private final PatronRepository patronRepository;


    private final BorrowingRecordMapper borrowingRecordMapper;

    public BorrowingRecordService(BorrowingRecordRepository borrowingRecordRepository, BookRepository bookRepository, PatronRepository patronRepository, BorrowingRecordMapper borrowingRecordMapper) {

        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
        this.borrowingRecordMapper = borrowingRecordMapper;
    }

    @Transactional
    public BorrowingRecordDTO returnBook(Long bookId,Long patronId) {
        BorrowingRecord record = borrowingRecordRepository
                .findByBookIdAndPatronIdAndReturnDateIsNull(bookId, patronId)
                .orElseThrow(() -> new ResourceNotFoundException("No active borrowing record found for this book and patron."));

        record.setReturnDate(LocalDate.now());
        try {
             borrowingRecordRepository.save(record);
             return borrowingRecordMapper.toDTO(record);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Failed to save return record: " + ex.getRootCause().getMessage());
        }
    }

    public List<BorrowingRecordDTO> getAllBorrowingRecords() {
        List<BorrowingRecord> records = borrowingRecordRepository.findAll();

        return records.stream()
                .map(borrowingRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

//    @Transactional
    public BorrowingRecordDTO borrowBook(Long bookId,Long patronId) {
        boolean isBookBorrowed = borrowingRecordRepository.existsByBookIdAndReturnDateIsNull(bookId);

        if (isBookBorrowed) {
            throw new IllegalArgumentException("This book is already borrowed and has not been returned yet.");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book with ID " + bookId + " not found"));

        Patron patron = patronRepository.findById(patronId)
                .orElseThrow(() -> new ResourceNotFoundException("Patron with ID " + patronId + " not found"));

        BorrowingRecord borrowingRecord = new BorrowingRecord();
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBook(book);
        borrowingRecord.setBorrowDate(LocalDate.now());
        borrowingRecord.setReturnDate(null);

        try {
             borrowingRecordRepository.save(borrowingRecord);
            return borrowingRecordMapper.toDTO(borrowingRecord);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Failed to save borrowing record: " + ex.getRootCause().getMessage());
        }catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while saving the borrowing record: " + ex.getMessage());
        }
    }

    public BorrowingRecord getBorrowingRecordById(Long id) {
        return borrowingRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrowing record not found with ID: " + id));
    }
}
