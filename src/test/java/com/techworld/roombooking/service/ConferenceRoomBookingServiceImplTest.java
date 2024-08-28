package com.techworld.roombooking.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.techworld.roombooking.entity.Booking;
import com.techworld.roombooking.entity.ConferenceRoom;
import com.techworld.roombooking.entity.MaintenanceInterval;
import com.techworld.roombooking.exception.CapacityException;
import com.techworld.roombooking.exception.InvalidDateException;
import com.techworld.roombooking.exception.InvalidTimeRangeException;
import com.techworld.roombooking.exception.NoRoomAvailableException;
import com.techworld.roombooking.model.BookingRequest;
import com.techworld.roombooking.model.BookingResponse;
import com.techworld.roombooking.repository.BookingRepository;
import com.techworld.roombooking.repository.ConferenceRoomRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Geeta Khatwani
 */

public class ConferenceRoomBookingServiceImplTest {

        @Mock
        private ConferenceRoomRepository conferenceRoomRepository;

        @Mock
        private BookingRepository bookingRepository;

        private ConferenceRoomBookingService conferenceRoomBookingService;

        AutoCloseable autoCloseable;

        @BeforeEach
        void setUp() {
                autoCloseable = MockitoAnnotations.openMocks(this);
                conferenceRoomBookingService = new ConferenceRoomBookingServiceImpl(conferenceRoomRepository,
                                bookingRepository);
        }

        @AfterEach
        void tearDown() {
        }

        @Test
        void testBookRoom_SuccessfulBooking() {

                LocalDate currentDate = LocalDate.now();
                String dateString = currentDate.toString(); // 2024-08-27"

                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);

                BookingRequest bookingRequest = new BookingRequest(dateString, 2,
                                startTime, endTime);

                List<MaintenanceInterval> maintenanceIntervals = Arrays.asList(
                                new MaintenanceInterval(LocalTime.of(9, 0), LocalTime.of(9, 15)),
                                new MaintenanceInterval(LocalTime.of(13, 0), LocalTime.of(13, 15)),
                                new MaintenanceInterval(LocalTime.of(17, 0), LocalTime.of(17, 15)));

                ConferenceRoom room = ConferenceRoom.builder()
                                .id(1L)
                                .name("Inspire")
                                .capacity(12)
                                .maintenanceIntervals(maintenanceIntervals)
                                .building("A").floor("5")
                                .build();

                List<ConferenceRoom> roomList = Arrays.asList(room);
                Booking booking = Booking.builder()
                                .conferenceRoom(room)
                                .bookingDate(LocalDate.parse(bookingRequest.date()))
                                .startTime(bookingRequest.startTime())
                                .endTime(bookingRequest.endTime())
                                .attendees(bookingRequest.attendees())
                                .build();

                when(bookingRepository.save(booking)).thenReturn(booking);

                when(conferenceRoomRepository.findAll()).thenReturn(roomList);

                BookingResponse response = conferenceRoomBookingService.bookRoom(bookingRequest);

                BookingResponse responseExpected = new BookingResponse();
                responseExpected.setMessage("Booking done for confrence room : " + room.getName() + " on " + dateString
                                + " from " + startTime
                                + "to "
                                + endTime);
                assertNotNull(response);
                assertThat(response)
                                .isEqualTo(responseExpected);
                verify(bookingRepository, times(1)).save(any(Booking.class));
        }

        @Test
        void testGetAvailableRooms_noBooking() {
                LocalDate bookingDate = LocalDate.now();
                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);
                String dateString = bookingDate.toString();

                List<MaintenanceInterval> maintenanceIntervals = Arrays.asList(
                                new MaintenanceInterval(LocalTime.of(9, 0), LocalTime.of(9, 15)),
                                new MaintenanceInterval(LocalTime.of(13, 0), LocalTime.of(13, 15)),
                                new MaintenanceInterval(LocalTime.of(17, 0), LocalTime.of(17, 15)));

                ConferenceRoom room = ConferenceRoom.builder()
                                .id(1L)
                                .name("Inspire")
                                .capacity(12)
                                .maintenanceIntervals(maintenanceIntervals)
                                .build();

                List<ConferenceRoom> conferenceRoomList = Arrays.asList(room);

                when(bookingRepository.findByConferenceRoomIdAndBookingDate(room.getId(),
                                bookingDate)).thenReturn(Collections.emptyList());

                when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);

                List<String> conferenceRoomsExist = Arrays
                                .asList(room.getName() + " conference room available on " + dateString + " from "
                                                + startTime
                                                + " to " + endTime);
                List<String> conferenceRooms = conferenceRoomBookingService.getAvailableRooms(bookingDate,
                                startTime,
                                endTime);

                assertNotNull(conferenceRooms);

                assertThat(conferenceRooms)
                                .isEqualTo(conferenceRoomsExist);
        }

        @Test
        void testGetAvailableRooms_withOverlap() {
                LocalDate bookingDate = LocalDate.now();
                LocalTime startTime = LocalTime.of(9, 0);
                LocalTime endTime = LocalTime.of(9, 15);

                List<MaintenanceInterval> maintenanceIntervals = Arrays.asList(
                                new MaintenanceInterval(LocalTime.of(9, 0), LocalTime.of(9, 15)),
                                new MaintenanceInterval(LocalTime.of(13, 0), LocalTime.of(13, 15)),
                                new MaintenanceInterval(LocalTime.of(17, 0), LocalTime.of(17, 15)));

                ConferenceRoom room = ConferenceRoom.builder()
                                .id(1L)
                                .name("Amaze")
                                .capacity(12)
                                .maintenanceIntervals(maintenanceIntervals).building("A").floor("5")
                                .build();

                List<ConferenceRoom> conferenceRoomList = Arrays.asList(room);

                when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);

                List<String> conferenceRooms = conferenceRoomBookingService.getAvailableRooms(bookingDate,
                                startTime,
                                endTime);
                assertNotNull(conferenceRooms);

        }

        @Test
        void testBookRoom_InvalidDateException() {
                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);

                BookingRequest bookingRequest = new BookingRequest("2024-08-25", 2,
                                startTime, endTime);

                InvalidDateException exception = assertThrows(InvalidDateException.class, () -> {
                        conferenceRoomBookingService.bookRoom(bookingRequest);
                });

                assertEquals("Booking can only be made for today's date.", exception.getMessage());
                verify(bookingRepository, never()).save(any(Booking.class));
        }

        @Test
        void testBookRoom_InvalidTimerangeException() {
                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(9, 0);

                BookingRequest bookingRequest = new BookingRequest("2024-08-26", 2,
                                startTime, endTime);

                InvalidTimeRangeException exception = assertThrows(InvalidTimeRangeException.class, () -> {
                        conferenceRoomBookingService.bookRoom(bookingRequest);
                });

                assertEquals("Invalid time range: Start time must be before end time.", exception.getMessage());
                verify(bookingRepository, never()).save(any(Booking.class));
        }

        @Test
        void testBookRoom_CapacityException() {

                LocalDate currentDate = LocalDate.now();
                String dateString = currentDate.toString();

                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);

                BookingRequest bookingRequest = new BookingRequest(dateString, 1,
                                startTime, endTime);

                CapacityException exception = assertThrows(CapacityException.class, () -> {
                        conferenceRoomBookingService.bookRoom(bookingRequest);
                });

                assertEquals("Attendees should be greater than 1.", exception.getMessage());
                verify(bookingRepository, never()).save(any(Booking.class));
        }

        @Test
        void testBookRoom_NoRoomAvailableException() {

                LocalDate currentDate = LocalDate.now();
                String dateString = currentDate.toString();

                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);

                BookingRequest bookingRequest = new BookingRequest(dateString, 25,
                                startTime, endTime);

                NoRoomAvailableException exception = assertThrows(NoRoomAvailableException.class, () -> {
                        conferenceRoomBookingService.bookRoom(bookingRequest);
                });

                assertEquals("No suitable room available for the requested time.", exception.getMessage());
                verify(bookingRepository, never()).save(any(Booking.class));
        }

        @Test
        void testfindSuitableRoom_noOverlap() {

                LocalDate bookingDate = LocalDate.now();
                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);
                String dateString = bookingDate.toString();

                List<MaintenanceInterval> maintenanceIntervals = Arrays.asList(
                                new MaintenanceInterval(LocalTime.of(9, 0), LocalTime.of(9, 15)),
                                new MaintenanceInterval(LocalTime.of(13, 0), LocalTime.of(13, 15)),
                                new MaintenanceInterval(LocalTime.of(17, 0), LocalTime.of(17, 15)));

                ConferenceRoom room = ConferenceRoom.builder()
                                .id(1L)
                                .name("Inspire")
                                .capacity(12)
                                .building("A").floor("5")
                                .maintenanceIntervals(maintenanceIntervals)
                                .build();

                List<ConferenceRoom> conferenceRoomList = Arrays.asList(room);

                List<String> conferenceRoomsExist = Arrays
                                .asList(room.getName() + " conference room available on " + dateString + " from "
                                                + startTime
                                                + " to " + endTime);

                when(bookingRepository.findByConferenceRoomIdAndBookingDate(room.getId(),
                                bookingDate)).thenReturn(Collections.emptyList());

                when(conferenceRoomRepository.findAll()).thenReturn(conferenceRoomList);

                List<String> conferenceRooms = conferenceRoomBookingService.getAvailableRooms(bookingDate,
                                startTime,
                                endTime);
                assertNotNull(conferenceRooms);

                assertThat(conferenceRooms)
                                .isEqualTo(conferenceRoomsExist);

        }

}
