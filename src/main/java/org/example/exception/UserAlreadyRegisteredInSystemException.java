package org.example.exception;

public class UserAlreadyRegisteredInSystemException extends RuntimeException {
    public UserAlreadyRegisteredInSystemException(String message) {
        super(message);
    }
}