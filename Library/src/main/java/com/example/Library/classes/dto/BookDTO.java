package com.example.Library.classes.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot be longer than 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 150, message = "Author name cannot be longer than 150 characters")
    private String author;

    @NotNull(message = "Publication Year is required")
    @Min(value = 1000, message = "Publication Year must be a valid year (e.g., >= 1000)")
    @Max(value = 9999, message = "Publication Year must be a valid year (e.g., <= 9999)")
    private int publicationYear;

    @NotBlank(message = "ISBN is required")
    @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters long")
    private String isbn;
}