package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */

public class BookingNotPossibleException extends BookingException {
    public BookingNotPossibleException(String message) {
        super(message, "ERR_BOOKING_NOT_POSSIBLE", HttpStatus.FORBIDDEN.value());
    }
}