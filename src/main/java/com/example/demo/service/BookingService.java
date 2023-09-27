package com.example.demo.service;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteById(String id) {
        bookingRepository.deleteById(id);
    }

    public List<Booking> findBookingsInDateRange(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findBookingsInDateRange(startDate, endDate);
    }

    public Optional<Booking> findById(String id) {
        return bookingRepository.findById(id);
    }
}
