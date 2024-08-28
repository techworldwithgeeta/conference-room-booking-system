package com.techworld.roombooking.service;

import org.springframework.stereotype.Service;

import com.techworld.roombooking.entity.Booking;
import com.techworld.roombooking.entity.ConferenceRoom;
import com.techworld.roombooking.entity.MaintenanceInterval;
import com.techworld.roombooking.exception.CapacityException;
import com.techworld.roombooking.exception.InvalidDateException;
import com.techworld.roombooking.exception.InvalidTimeRangeException;
import com.techworld.roombooking.exception.MaintenanceConflictException;
import com.techworld.roombooking.exception.NoRoomAvailableException;
import com.techworld.roombooking.exception.RoomAlreadyBookedException;
import com.techworld.roombooking.model.BookingRequest;
import com.techworld.roombooking.model.BookingResponse;
import com.techworld.roombooking.repository.BookingRepository;
import com.techworld.roombooking.repository.ConferenceRoomRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
// @RequiredArgsConstructor
public class ConferenceRoomBookingServiceImpl implements ConferenceRoomBookingService {

    private ConferenceRoomRepository conferenceRoomRepository;
    private BookingRepository bookingRepository;

    public ConferenceRoomBookingServiceImpl(ConferenceRoomRepository conferenceRoomRepository,
            BookingRepository bookingRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * This method is used for booikng room
     */
    public BookingResponse bookRoom(BookingRequest bookingRequest) {

        LocalDate bookingDate = LocalDate.parse(bookingRequest.date());

        LocalTime startTime = (bookingRequest.startTime());
        LocalTime endTime = (bookingRequest.endTime());

        validateBookingDetails(bookingRequest);

        // Rule 4: Booking should be optimal (select the smallest suitable room)
        Optional<ConferenceRoom> optimalRoom = findSuitableRoom(bookingRequest);

        if (optimalRoom.isEmpty()) {
            throw new NoRoomAvailableException("No suitable room available for the requested time.");
        }

        // Check if the selected room can accommodate the number of people (Max capacity
        // check)
        ConferenceRoom room = optimalRoom.get();
        if (bookingRequest.attendees() > room.getCapacity()) {
            throw new CapacityException("The number of attendees exceeds the room's maximum capacity.");
        }

        // Rule 5: Booking cannot be done during maintenance time
        if (isOverlappingMaintenanceTime(room, startTime, endTime)) {
            throw new MaintenanceConflictException("Booking cannot be done during maintenance time.");
        }

        // check if the room is already booked
        if (isAlreadyBooked(room, bookingDate, startTime, endTime)) {
            throw new RoomAlreadyBookedException("The room is already booked");
        }

        // persist this details in database
        Booking booking = Booking.builder().bookingDate(bookingDate)
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .conferenceRoom(room)
                .attendees(bookingRequest.attendees())
                .build();

        // Save the booking in the database
        bookingRepository.save(booking);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setMessage(
                "Booking done for confrence room : " + room.getName() + " on " + bookingDate + " from " + startTime
                        + "to "
                        + endTime);
        return bookingResponse;
    }

    /**
     * This method is used to find the available rooms.
     */
    public List<String> getAvailableRooms(LocalDate bookingDate, LocalTime startTime, LocalTime endTime) {
        List<String> conferenceRooms = conferenceRoomRepository.findAll().stream()
                .filter(room -> isRoomAvailable(room, bookingDate, startTime, endTime))
                .sorted((r1, r2) -> Integer.compare(r1.getCapacity(), r2.getCapacity()))
                .map(room -> room.getName() + " conference room available on " + bookingDate + " from " + startTime
                        + " to " + endTime)
                .collect(Collectors.toList());
        return conferenceRooms;
    }

    private Optional<ConferenceRoom> findSuitableRoom(BookingRequest bookingRequest) {

        LocalDate bookingDate = LocalDate.parse(bookingRequest.date());

        LocalTime startTime = bookingRequest.startTime();
        LocalTime endTime = bookingRequest.endTime();

        List<ConferenceRoom> suitableRooms = conferenceRoomRepository.findAll().stream()
                .filter(room -> room.getCapacity() >= bookingRequest.attendees())
                .filter(room -> isRoomAvailable(room, bookingDate, startTime, endTime))
                .sorted((r1, r2) -> Integer.compare(r1.getCapacity(), r2.getCapacity()))
                .collect(Collectors.toList());

        if (!suitableRooms.isEmpty()) {
            ConferenceRoom roomToBook = suitableRooms.get(0);
            return Optional.of(roomToBook);
        }
        return Optional.empty();
    }

    private boolean isAlreadyBooked(ConferenceRoom room, LocalDate bookingDate, LocalTime startTime,
            LocalTime endTime) {
        List<Booking> bookings = bookingRepository.findByConferenceRoomAndBookingDate(room, LocalDate.now());
        for (Booking booking : bookings) {

            LocalDate existingDate = booking.getBookingDate();
            LocalTime existingStartTime = booking.getStartTime();
            LocalTime existingEndTime = booking.getEndTime();

            if (bookingDate.equals(existingDate) &&
                    startTime.isBefore(existingEndTime) && endTime.isAfter(existingStartTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRoomAvailable(ConferenceRoom room, LocalDate bookingDate, LocalTime startTime,
            LocalTime endTime) {
        // Check if the requested time overlaps with maintenance intervals
        for (MaintenanceInterval interval : room.getMaintenanceIntervals()) {
            if (timeOverlaps(startTime, endTime, interval.getStartTime(), interval.getEndTime())) {
                return false;
            }
        }

        // Fetch bookings for the room on the specified date
        List<Booking> bookings = bookingRepository
                .findByConferenceRoomIdAndBookingDate(room.getId(), bookingDate);

        // Check if any existing booking overlaps with the requested time range
        for (Booking booking : bookings) {
            if (timeOverlaps(startTime, endTime, booking.getStartTime(), booking.getEndTime())) {
                return false; // Room is not available
            }
        }
        return true; // Room is available
    }

    private boolean timeOverlaps(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private void validateBookingDetails(BookingRequest bookingRequest) {
        LocalDate bookingDate = LocalDate.parse(bookingRequest.date());
        LocalDate today = LocalDate.now();
        LocalTime startTime = (bookingRequest.startTime());
        LocalTime endTime = (bookingRequest.endTime());

        // Rule 1: Booking can only be done for the current date

        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new InvalidTimeRangeException("Invalid time range: Start time must be before end time.");
        }
        if (!bookingDate.equals(today)) {
            throw new InvalidDateException("Booking can only be made for today's date.");
        }

        // Rule 2: Booking time should be in intervals of 15 minutes
        if (!isTimeIntervalValid(startTime, endTime)) {
            throw new InvalidTimeRangeException("Booking time must be in intervals of 15 minutes.");
        }

        // Rule 3: Booking happens on First Come First Serve (handled naturally by
        // booking logic)

        // Rule 6: Number of people should be greater than 1 and less than or equal to
        // max capacity
        if (bookingRequest.attendees() <= 1) {
            // return ResponseEntity.ok("The number of people should be greater than 1.");
            throw new CapacityException("Attendees should be greater than 1.");
        }

    }

    private boolean isOverlappingMaintenanceTime(ConferenceRoom room, LocalTime startTime, LocalTime endTime) {
        for (MaintenanceInterval interval : room.getMaintenanceIntervals()) {
            LocalTime maintenanceStart = interval.getStartTime();
            LocalTime maintenanceEnd = interval.getEndTime();
            if (startTime.isBefore(maintenanceEnd) && endTime.isAfter(maintenanceStart)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeIntervalValid(LocalTime startTime, LocalTime endTime) {
        long minutes = startTime.until(endTime, ChronoUnit.MINUTES);
        return minutes % 15 == 0;
    }
}
