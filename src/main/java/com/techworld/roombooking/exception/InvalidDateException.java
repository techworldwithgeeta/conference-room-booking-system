package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */
public class InvalidDateException extends BookingException {
    public InvalidDateException(String message) {
        super(message, "ERR_INVALID_DATE", HttpStatus.BAD_REQUEST.value());
    }
}
