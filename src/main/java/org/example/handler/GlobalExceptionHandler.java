package org.example.handler;

import org.example.dto.ErrorMessage;
import org.example.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({PersonIsNotAdultException.class, PeselNotValidException.class, MethodArgumentNotValidException.class})
    public final ResponseEntity<ErrorMessage> handleAllExceptionsAboutPesel(Exception ex, WebRequest request) {
        var errorMessage = ErrorMessage.builder()
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchInformationFromNbpException.class)
    public final ResponseEntity<ErrorMessage> handleNoSuchInformationFromNbpException(Exception ex, WebRequest request) {
        var errorMessage = ErrorMessage.builder()
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyRegisteredInSystemException.class)
    public final ResponseEntity<ErrorMessage> handleUserAlreadyRegisteredInSystemException(Exception ex, WebRequest request) {
        var errorMessage = ErrorMessage.builder()
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorMessage> handleUserNotFoundException(Exception ex, WebRequest request) {
        var errorMessage = ErrorMessage.builder()
                .message(ex.getMessage()).build();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
