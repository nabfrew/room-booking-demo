package com.example.demo.controller;

import com.example.demo.model.Room;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public List<Room> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/available")
    public List<Room> findAvailableRooms(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("numberOfBeds") int numberOfBeds
    ) {
        return roomService.findAvailableRooms(startDate, endDate, numberOfBeds);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public Room create(@RequestBody Room room) {
        return roomService.save(room);
    }

    @PutMapping
    public Room update(@RequestBody Room room) {
        return roomService.save(room);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        roomService.deleteById(id);
    }

}
