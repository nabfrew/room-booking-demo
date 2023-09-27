package com.example.demo.controller;

import com.example.demo.dto.CreateBookingResponse;
import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/booking")
public class BookingController {

    // Arbitrary range to for effectively unbounded ranges that don't break postgres.
    // TODO: dedicated queries using BEFORE/AFTER instead?
    private static final LocalDate START_OF_TIME = LocalDate.of(0,1,1);
    private static final LocalDate END_OF_TIME = LocalDate.of(10_000,1,1);
    public static final String ROOM_UNAVAILABLE_MSG = "Room is not available for the requested period.";

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoomService roomService;

    @GetMapping("/{id}")
    public Optional<Booking> findById(@PathVariable String id) {
        return bookingService.findById(id);
    }

    @GetMapping
    public List<Booking> findAllInRange(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null && endDate== null) {
            return bookingService.findAll();
        }
        startDate = startDate == null ? START_OF_TIME : startDate;
        endDate = endDate == null ? END_OF_TIME : endDate;

        return bookingService.findBookingsInDateRange(startDate, endDate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<CreateBookingResponse> createBooking(@RequestBody Booking booking) {
        try {
            var persistedBooking = bookingService.save(booking);
            var successResponse = new CreateBookingResponse(true, persistedBooking.getId(), "");
            return new ResponseEntity<>(successResponse, CREATED);
        } catch (DataIntegrityViolationException ex) {
            // I did the fancy database validation and end up implementing the checks here anyway in order to get useful error messages.
            // There's probably a better way.
            var failedResponseMessage = "unknown data integrity issue";
            if (booking.getStartDate().isAfter(booking.getEndDate())) {
                failedResponseMessage = "Start date can't be before end date";
            } else if(!roomService.findAll().contains(booking.getRoom()))  {
                failedResponseMessage = "Room not found";
            } else if (!roomService.findAvailableRooms(booking.getStartDate(), booking.getEndDate(), 0).contains(booking.getRoom())) {
                failedResponseMessage = "Room not available in requested period.";
            }
            var failedResponse = new CreateBookingResponse(false, "", failedResponseMessage);
            return new ResponseEntity<>(failedResponse, BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        bookingService.deleteById(id);
    }

}
