package org.example.exception;

public class PeselNotValidException extends RuntimeException {
    public PeselNotValidException(String message) {
        super(message);
    }
}