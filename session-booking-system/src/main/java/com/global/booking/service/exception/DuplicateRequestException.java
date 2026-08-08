package com.global.booking.service.exception;

public class DuplicateRequestException
        extends RuntimeException {

    public DuplicateRequestException(String message) {
        super(message);
    }
}
