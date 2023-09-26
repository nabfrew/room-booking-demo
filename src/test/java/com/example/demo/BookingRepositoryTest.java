package com.example.demo;

import com.example.demo.model.Booking;
import com.example.demo.model.Room;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BookingRepositoryTest {

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
    void testDoubleBookingDisallowed() {
        var room = new Room("1", "room1", "room", 5, new BigDecimal(5));
        var persistedRoom = roomRepository.save(room);

        var booking1 = new Booking(LocalDate.of(2020, 1,1), LocalDate.of(2020, 1, 1), persistedRoom);
        var booking2 = new Booking(LocalDate.of(2020, 1,2), LocalDate.of(2020, 1, 1), persistedRoom);

        bookingRepository.save(booking1);
        assertThrows(DataIntegrityViolationException.class, () -> bookingRepository.save(booking2));

    }
}