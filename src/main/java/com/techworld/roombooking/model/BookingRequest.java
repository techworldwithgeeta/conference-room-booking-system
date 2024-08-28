package com.techworld.roombooking.model;

import java.time.LocalTime;

import lombok.Builder;

/**
 * @author Geeta Khatwani
 */

@Builder
public record BookingRequest(String date, int attendees, LocalTime startTime, LocalTime endTime) {
}
