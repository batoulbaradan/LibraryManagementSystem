package com.example.Library.repository.table;

import com.example.Library.model.table.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findByBookIdAndPatronIdAndReturnDateIsNull(Long bookId, Long patronId);
    Optional<BorrowingRecord> findByBookIdAndReturnDateIsNull(Long bookId);
    boolean existsByBookIdAndReturnDateIsNull(Long bookId);

}