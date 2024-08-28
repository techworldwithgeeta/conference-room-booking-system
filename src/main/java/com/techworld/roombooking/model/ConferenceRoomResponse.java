package com.techworld.roombooking.model;

import java.time.LocalTime;
import java.util.List;

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
public class ConferenceRoomResponse {

    private Long id;
    private String name;
    private int capacity;
    private List<LocalTime> maintenanceTimes;
}
