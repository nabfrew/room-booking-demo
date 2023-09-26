package com.example.demo.service;

import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomRepository bookingRepository;
}
