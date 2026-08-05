package com.shurutech.reservation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            CapacityExceededException.class)
    public ResponseEntity<String> handleCapacity(
            CapacityExceededException ex) {

        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}