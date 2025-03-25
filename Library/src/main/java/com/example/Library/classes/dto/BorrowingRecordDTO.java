package com.example.Library.classes.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowingRecordDTO {

    private Long id;

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotNull(message = "Patron ID is required")
    private Long patronId;

    @NotNull(message = "Borrow date is required")
    @PastOrPresent(message = "Borrow date must be in the past or present")
    private LocalDate borrowDate;

    @FutureOrPresent(message = "Return date must be in the future or present")
    private LocalDate returnDate;
}