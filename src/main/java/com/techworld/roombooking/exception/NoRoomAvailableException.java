package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */
public class NoRoomAvailableException extends BookingException {
    public NoRoomAvailableException(String message) {
        super(message, "NO_ROOM_AVAILABLE", HttpStatus.NOT_FOUND.value());
    }
}