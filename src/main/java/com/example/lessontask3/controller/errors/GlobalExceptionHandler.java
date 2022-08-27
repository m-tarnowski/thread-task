package com.example.lessontask3.controller.errors;


import com.example.lessontask3.exceptions.PrintingThreadNotFoundException;
import com.example.lessontask3.exceptions.ThreadCannotCanceledException;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PrintingThreadNotFoundException.class)
    public final ResponseEntity handlePrintingThreadNotFoundException(PrintingThreadNotFoundException ex) {
        return new ResponseEntity(new ValidationErrorDto(ex.getId(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ThreadCannotCanceledException.class)
    public final ResponseEntity handleThreadCannotCanceledException(ThreadCannotCanceledException ex) {
        return new ResponseEntity(new ValidationErrorDto(ex.getId(), ex.getThreadState()), HttpStatus.BAD_REQUEST);
    }

    @Value
    static class ValidationErrorDto {
        long id;
        String message;
    }
}
