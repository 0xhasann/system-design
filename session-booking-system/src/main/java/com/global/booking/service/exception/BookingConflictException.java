package com.global.booking.service.exception;

public class BookingConflictException
        extends RuntimeException {

    public BookingConflictException(String message) {
        super(message);
    }
}
