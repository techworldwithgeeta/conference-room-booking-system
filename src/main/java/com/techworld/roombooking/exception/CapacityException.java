package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */
public class CapacityException
        extends BookingException {

    public CapacityException(String message) {
        super(message, "ERR_CAPACITY_ISSUE", HttpStatus.BAD_REQUEST.value());
    }
}