package com.example.Library.restController.table;

import com.example.Library.classes.dto.BorrowingRecordDTO;
import com.example.Library.classes.response.ApiResponse;
import com.example.Library.service.table.BorrowingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BorrowingRecordRestController {
    @Autowired
    private BorrowingRecordService borrowingRecordService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowingRecordDTO>>> getAllBorrowingRecords() {
        List<BorrowingRecordDTO> records = borrowingRecordService.getAllBorrowingRecords();
        return ResponseEntity.ok(new ApiResponse<>(true, "Borrowing records retrieved successfully", records));
    }

    @PostMapping("/borrow/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse<BorrowingRecordDTO>> borrowBook(@PathVariable Long bookId,
                                                                      @PathVariable Long patronId) {
        BorrowingRecordDTO record = borrowingRecordService.borrowBook(bookId, patronId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Book borrowed successfully", record));
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse<BorrowingRecordDTO>> returnBook(@PathVariable Long bookId,
                                                                      @PathVariable Long patronId) {
        BorrowingRecordDTO returnedRecord = borrowingRecordService.returnBook(bookId, patronId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Book returned successfully", returnedRecord));
    }
}

