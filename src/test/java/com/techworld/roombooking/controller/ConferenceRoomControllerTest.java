package com.techworld.roombooking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.techworld.roombooking.entity.ConferenceRoom;
import com.techworld.roombooking.model.BookingRequest;
import com.techworld.roombooking.model.BookingResponse;
import com.techworld.roombooking.service.ConferenceRoomBookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Geeta Khatwani
 */

@WebMvcTest(ConferenceRoomBookingController.class)
class ConferenceRoomControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ConferenceRoomBookingServiceImpl conferenceRoomBookingService;

        @InjectMocks
        private ConferenceRoomBookingController conferenceRoomController;

        @BeforeEach
        void setUp() {
        }

        @Test
        void testBookRoom() throws Exception {
                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);

                BookingRequest bookingRequest = new BookingRequest("2024-08-27", 1,
                                startTime, endTime);

                ConferenceRoom room = ConferenceRoom.builder()
                                .id(1L)
                                .name("Amaze")
                                .capacity(10)
                                .build();

                BookingResponse bookingResponse = BookingResponse.builder()
                                .message("Booking done for room " + room.getName())
                                .build();

                when(conferenceRoomBookingService.bookRoom(bookingRequest)).thenReturn(bookingResponse);

                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(bookingRequest);

                this.mockMvc.perform(post("/api/conference-rooms/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andDo(print()).andExpect(status().isCreated());

                verify(conferenceRoomBookingService, times(1)).bookRoom(any(BookingRequest.class));
        }

        @Test
        void testBookRoom_Exception() throws Exception {

                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);

                BookingRequest bookingRequest = new BookingRequest("2024-08-26", 1,
                                startTime, endTime);

                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();
                ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
                String requestJson = ow.writeValueAsString(bookingRequest);

                when(conferenceRoomBookingService.bookRoom(any(BookingRequest.class)))
                                .thenThrow(new RuntimeException("Booking failed"));

                this.mockMvc.perform(post("/api/conference-rooms/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson))
                                .andDo(print()).andExpect(status().isInternalServerError());

                verify(conferenceRoomBookingService,
                                times(1)).bookRoom(any(BookingRequest.class));
        }

        @Test
        void testGetAvailableRooms() throws Exception {
                LocalDate bookingDate = LocalDate.now();
                LocalTime startTime = LocalTime.of(10, 0);
                LocalTime endTime = LocalTime.of(11, 0);
                LocalDate date = LocalDate.now();

                List<String> availableRoomsList = Arrays.asList("Inspire");

                when(conferenceRoomBookingService.getAvailableRooms(bookingDate, startTime,
                                endTime)).thenReturn(availableRoomsList);

                mockMvc.perform(get("/api/conference-rooms/available")
                                .param("startTime", startTime.toString())
                                .param("endTime", endTime.toString())
                                .param("bookingDate", date.toString()))
                                .andExpect(status().isOk());

                verify(conferenceRoomBookingService, times(1)).getAvailableRooms(bookingDate, startTime,
                                endTime);
        }

}
