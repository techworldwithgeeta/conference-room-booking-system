package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Geeta Khatwani
 */
public class MaintenanceConflictException extends BookingException {
    public MaintenanceConflictException(String message) {
        super(message, "ERR_MAINTENANCE_CONFLICT", HttpStatus.CONFLICT.value());
    }
}