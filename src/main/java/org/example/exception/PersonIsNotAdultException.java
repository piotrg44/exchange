package org.example.exception;

public class PersonIsNotAdultException extends RuntimeException {
    public PersonIsNotAdultException(String message) {
        super(message);
    }
}