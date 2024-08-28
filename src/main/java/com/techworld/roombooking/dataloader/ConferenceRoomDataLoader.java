package com.techworld.roombooking.dataloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.techworld.roombooking.entity.ConferenceRoom;
import com.techworld.roombooking.entity.MaintenanceInterval;
import com.techworld.roombooking.repository.ConferenceRoomRepository;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Geeta Khatwani
 */

@Component
public class ConferenceRoomDataLoader
        implements CommandLineRunner {

    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;

    @Override
    public void run(String... args) throws Exception {
        if (conferenceRoomRepository.count() == 0) {

            List<MaintenanceInterval> maintenanceIntervals = Arrays.asList(
                    new MaintenanceInterval(LocalTime.of(9, 0), LocalTime.of(9, 15)),
                    new MaintenanceInterval(LocalTime.of(13, 0), LocalTime.of(13, 15)),
                    new MaintenanceInterval(LocalTime.of(17, 0), LocalTime.of(17, 15)));

            ConferenceRoom conferenceRoom1 = new ConferenceRoom(1l, "Amaze", 3, "5", "A", maintenanceIntervals);
            ConferenceRoom conferenceRoom2 = new ConferenceRoom(2l, "Beauty", 7, "5", "A", maintenanceIntervals);
            ConferenceRoom conferenceRoom3 = new ConferenceRoom(3l, "Inspire", 12, "5", "A", maintenanceIntervals);
            ConferenceRoom conferenceRoom4 = new ConferenceRoom(4l, "Strive", 20, "5", "A", maintenanceIntervals);

            conferenceRoomRepository
                    .saveAll(Arrays.asList(conferenceRoom1, conferenceRoom2, conferenceRoom3, conferenceRoom4));
        }
    }
}
