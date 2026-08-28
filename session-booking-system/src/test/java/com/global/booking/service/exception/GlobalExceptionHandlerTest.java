package com.global.booking.service.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.global.booking.service.dto.response.ApiResponse;

import jakarta.persistence.OptimisticLockException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound() {
        ResponseEntity<ApiResponse<?>> response = handler.handleNotFound(
                new ResourceNotFoundException("Course not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Course not found");
    }

    @Test
    void handleConflict() {
        ResponseEntity<ApiResponse<?>> response = handler.handleConflict(
                new BookingConflictException("Offering already booked"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Offering already booked");
    }

    @Test
    void handleValidation() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
                new Object(), "request");
        bindingResult.addError(new FieldError("request", "parentId", "Parent Id is required"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ApiResponse<?>> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Parent Id is required");
    }

    @Test
    void handleOptimisticLock() {
        ResponseEntity<ApiResponse<?>> response = handler.handleOptimisticLock(
                new OptimisticLockException());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isFalse();
        assertThat(response.getBody().getMessage()).isEqualTo("Concurrent modification detected");
    }
}
