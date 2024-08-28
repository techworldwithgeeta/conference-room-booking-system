package com.techworld.roombooking.entity;

import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Geeta Khatwani
 */

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceInterval {
    private LocalTime startTime;
    private LocalTime endTime;
}
