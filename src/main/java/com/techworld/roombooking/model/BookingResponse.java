package com.techworld.roombooking.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.techworld.roombooking.entity.ConferenceRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Geeta Khatwani
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private String message;

}
