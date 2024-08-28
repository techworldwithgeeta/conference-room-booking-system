package com.techworld.roombooking.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techworld.roombooking.entity.Booking;
import com.techworld.roombooking.entity.ConferenceRoom;

/**
 * @author Geeta Khatwani
 */

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByConferenceRoomAndBookingDate(ConferenceRoom conferenceRoom, LocalDate bookingDate);

    // List<Booking>
    // findByConferenceRoomIdAndStartTimeLessThanAndEndTimeGreaterThan(Long roomId,
    // LocalTime startTime,
    // LocalTime endTime);

    // List<Booking>
    // findByConferenceRoomIdAndBookingDateAndStartTimeLessThanAndEndTimeGreaterThan(Long
    // roomId,
    // LocalDate bookingDate,
    // LocalTime startTime,
    // LocalTime endTime);

    List<Booking> findByConferenceRoomIdAndBookingDate(Long conferenceRoomId, LocalDate bookingDate);

}
