package com.example.demo.repository;

import com.example.demo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {
    @Query("SELECT b FROM Booking b WHERE b.startDate <= :endDate AND b.endDate >= :startDate")
    List<Booking> findBookingsInDateRange(LocalDate startDate, LocalDate endDate);
}
