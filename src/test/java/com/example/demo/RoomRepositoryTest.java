package com.example.demo;

import com.example.demo.model.Booking;
import com.example.demo.model.Room;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void testSaveAndFetchAll() {
        var inputRoom1 = new Room("101", "executive suite", "nice room", 2, new BigDecimal(3));
        var inputRoom2 = new Room("237", "basement", "horrible room", 1, new BigDecimal("0.1"));
        roomRepository.save(inputRoom1);
        roomRepository.save(inputRoom2);
        var persistedRooms = roomRepository.findAll();
        assertTrue(persistedRooms.stream().anyMatch(room -> room.equivalent(inputRoom1)));
        assertTrue(persistedRooms.stream().anyMatch(room -> room.equivalent(inputRoom2)));
    }

    @Test
    void testFindAvailable() {
        var inputRoom1 = new Room("101", "executive suite", "nice room", 2, new BigDecimal(3));
        var inputRoom2 = new Room("237", "basement", "horrible room", 1, new BigDecimal("0.1"));

        var persistedRoom1 = roomRepository.save(inputRoom1);
        var persistedRoom2 = roomRepository.save(inputRoom2);

        var bookingStart = LocalDate.of(2023, 1, 1);
        var bookingEnd = LocalDate.of(2023, 1, 3);

        var bookingRoom1 = new Booking(bookingStart, bookingEnd, persistedRoom1);
        bookingRepository.save(bookingRoom1);

        // Both are eligible by nr. beds, testing that existing booking excludes room 1.
        var availableRooms = roomRepository.findAvailableRooms(bookingStart, bookingEnd, 1);
        assertEquals(1, availableRooms.size());
        assertEquals(persistedRoom2, availableRooms.get(0));

        // Test a partial overlap, room 1 should still be excluded.
        availableRooms = roomRepository.findAvailableRooms(bookingStart.plusDays(1), bookingEnd.plusDays(1), 1);
        assertEquals(1, availableRooms.size());
        assertEquals(persistedRoom2, availableRooms.get(0));

        // Test a partial overlap
        availableRooms = roomRepository.findAvailableRooms(bookingStart.minusDays(1), bookingEnd.minusDays(1), 1);
        assertEquals(1, availableRooms.size());
        assertEquals(persistedRoom2, availableRooms.get(0));

        // Now both should be excluded, room 1 for the dates, room 2 for the beds.
        availableRooms = roomRepository.findAvailableRooms(bookingStart, bookingEnd, 2);
        assertEquals(0, availableRooms.size());
    }
}