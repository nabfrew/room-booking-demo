package com.example.demo.repository;

import com.example.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("SELECT r FROM Room r " +
            "WHERE r.beds >= :numberOfBeds " +
            "AND r.id NOT IN " +
            "(SELECT b.room.id FROM Booking b " +
            "WHERE (:startDate BETWEEN b.startDate AND b.endDate) " +
            "OR (:endDate BETWEEN b.startDate AND b.endDate) " +
            "OR (b.startDate BETWEEN :startDate AND :endDate))")
    List<Room> findAvailableRooms(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("numberOfBeds") int numberOfBeds
    );


}
