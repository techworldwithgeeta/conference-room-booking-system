package com.techworld.roombooking.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Geeta Khatwani
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConferenceRoom {

    @Id
    private Long id;

    private String name;
    private int capacity;
    private String floor;
    private String building;

    @ElementCollection
    private List<MaintenanceInterval> maintenanceIntervals;

}
