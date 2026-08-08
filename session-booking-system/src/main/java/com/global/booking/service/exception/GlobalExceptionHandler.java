
package com.global.booking.service.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.global.booking.service.dto.response.ApiResponse;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiResponse<?>> handleNotFound(
                        ResourceNotFoundException ex) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(
                                                ApiResponse.builder()
                                                                .success(false)
                                                                .message(ex.getMessage())
                                                                .build());
        }

        @ExceptionHandler(BookingConflictException.class)
        public ResponseEntity<ApiResponse<?>> handleConflict(
                        BookingConflictException ex) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(
                                                ApiResponse.builder()
                                                                .success(false)
                                                                .message(ex.getMessage())
                                                                .build());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<?>> handleValidation(
                        MethodArgumentNotValidException ex) {

                String message = ex.getBindingResult()
                                .getFieldError()
                                .getDefaultMessage();

                return ResponseEntity.badRequest()
                                .body(
                                                ApiResponse.builder()
                                                                .success(false)
                                                                .message(message)
                                                                .build());
        }

        @ExceptionHandler(OptimisticLockException.class)
        public ResponseEntity<ApiResponse<?>> handleOptimisticLock(
                        OptimisticLockException ex) {

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(
                                                ApiResponse.builder()
                                                                .success(false)
                                                                .message("Concurrent modification detected")
                                                                .build());
        }

}
