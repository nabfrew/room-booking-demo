package com.example.demo.service;

import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findById(String id) {
        return roomRepository.findById(id);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteById(String id) {
        roomRepository.deleteById(id);
    }

    public List<Room> findAvailableRooms(LocalDate startDate, LocalDate endDate, int numberOfBeds) {
        return roomRepository.findAvailableRooms(startDate, endDate, numberOfBeds);
    }
}
