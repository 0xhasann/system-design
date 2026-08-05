package com.shurutech.reservation.exception;

public class CapacityExceededException
        extends RuntimeException {

    public CapacityExceededException(String message) {
        super(message);
    }
}
