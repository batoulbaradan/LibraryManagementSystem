package com.example.Library.exception;

public class DuplicateISBNException extends RuntimeException {
    public DuplicateISBNException(String message) {
        super(message);
    }
}