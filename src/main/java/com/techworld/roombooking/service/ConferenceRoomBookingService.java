package com.techworld.roombooking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.techworld.roombooking.model.BookingRequest;
import com.techworld.roombooking.model.BookingResponse;

/**
 * @author Geeta Khatwani
 */

public interface ConferenceRoomBookingService {

    BookingResponse bookRoom(BookingRequest bookingRequest);

    List<String> getAvailableRooms(LocalDate date, LocalTime startTime, LocalTime endTime);

}
