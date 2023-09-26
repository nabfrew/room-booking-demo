package com.example.demo;

import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
// activate automatic startup and stop of containers
// JPA drop and create table, good for testing
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @BeforeEach
    void setup() {
        roomRepository.deleteAll();
    }

    @Test
    void testSave() {
        Room inputRoom1 = new Room("101", "executive suite", "nice room", 2, new BigDecimal(3));
        Room inputRoom2 = new Room("237", "basement", "horrible room", 1, new BigDecimal("0.1"));
        roomRepository.save(inputRoom1);
        roomRepository.save(inputRoom2);
        var persistedRooms = roomRepository.findAll();
        assertTrue(persistedRooms.stream().anyMatch(room -> room.equivalent(inputRoom1)));
        assertTrue(persistedRooms.stream().anyMatch(room -> room.equivalent(inputRoom2)));
    }
}