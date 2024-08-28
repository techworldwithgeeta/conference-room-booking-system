package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */
public class RoomAlreadyBookedException extends BookingException {
    public RoomAlreadyBookedException(String message) {
        super(message, "ERR_ROOM_ALREADY_BOOKED", HttpStatus.NOT_FOUND.value());
    }
}