package com.example.demo;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.example.demo.model.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class RoomControllerTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON parsing

    @BeforeEach
    private void setup() {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
    }

    @Test
    void testFindAvailableRooms() throws Exception {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        int numberOfBeds = 2;

        // Add the room and bookings directly
        var room1 = roomRepository.save(new Room("1", "one", "first", 1, new BigDecimal(1)));
        var room2 = roomRepository.save(new Room("2", "two", "second", 2, new BigDecimal(2)));

        // partial overlap
        bookingRepository.save(new Booking(startDate.plusDays(4), endDate.plusDays(4), room1));


        // Perform a GET request to your controller method
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/room/available")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .param("numberOfBeds", String.valueOf(numberOfBeds))
                        .contentType(APPLICATION_JSON))
                .andReturn();

        // Extract and parse the response content
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        List<Room> rooms = objectMapper.readValue(jsonResponse, new TypeReference<List<Room>>() {});

        assertEquals(1, rooms.size());
        assertTrue(room2.equivalent(rooms.get(0)));
    }
}