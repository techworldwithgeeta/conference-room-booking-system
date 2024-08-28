package com.techworld.roombooking.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Geeta Khatwani
 */

@Data
@NoArgsConstructor
public class BookingException extends RuntimeException {

    private String errorCode;
    private int status;

    public BookingException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

}
