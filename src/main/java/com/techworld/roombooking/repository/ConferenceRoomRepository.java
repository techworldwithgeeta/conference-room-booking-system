package com.techworld.roombooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techworld.roombooking.entity.ConferenceRoom;

/**
 * @author Geeta Khatwani
 */

public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {

}
