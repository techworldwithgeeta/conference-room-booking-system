package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */
public class InvalidTimeRangeException extends BookingException {
    public InvalidTimeRangeException(String message) {
        super(message, "ERR_INVALID_TIME_RANGE", HttpStatus.BAD_REQUEST.value());
    }
}