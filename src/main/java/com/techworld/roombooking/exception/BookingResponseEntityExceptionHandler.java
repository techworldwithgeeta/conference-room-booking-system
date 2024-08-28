package com.techworld.roombooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.techworld.roombooking.model.ErrorResponse;

/**
 * @author Geeta Khatwani
 */

@ControllerAdvice
public class BookingResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BookingException exception) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(exception.getMessage())
                .errorCode(exception.getErrorCode())
                .build(),
                HttpStatus.valueOf(exception.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .errorCode("INTERNAL_SERVER_ERROR")
                .build(),
                HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

}
