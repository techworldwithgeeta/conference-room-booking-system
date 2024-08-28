package com.techworld.roombooking.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.techworld.roombooking.model.BookingRequest;
import com.techworld.roombooking.model.BookingResponse;
import com.techworld.roombooking.service.ConferenceRoomBookingService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Geeta Khatwani
 */

@RestController
@RequestMapping("/api/conference-rooms")
public class ConferenceRoomBookingController {

    private ConferenceRoomBookingService conferenceRoomBookingService;

    public ConferenceRoomBookingController(ConferenceRoomBookingService conferenceRoomBookingService) {
        this.conferenceRoomBookingService = conferenceRoomBookingService;
    }

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> bookRoom(@RequestBody BookingRequest bookingRequest) throws Exception {
        BookingResponse bookingResponse = conferenceRoomBookingService.bookRoom(bookingRequest);
        return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);

    }

    @GetMapping("/available")
    public ResponseEntity<List<String>> getAvailableRooms(
            @RequestParam(value = "bookingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bookingDate,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        List<String> availableRooms = conferenceRoomBookingService.getAvailableRooms(bookingDate, startTime,
                endTime);
        return ResponseEntity.ok(availableRooms);
    }
}
