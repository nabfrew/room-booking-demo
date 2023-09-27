package com.example.demo.ControllerTest;

import com.example.demo.dto.CreateBookingResponse;
import com.example.demo.model.Booking;
import com.example.demo.model.Room;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.demo.controller.BookingController.ROOM_UNAVAILABLE_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class BookingControllerTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking booking1;
    private Booking booking2;
    private Room unbookedRoom;
    private Room occupiedRoom;
    private Room occupiedRoom2;
    private final LocalDate currentDate = LocalDate.now();

    @BeforeEach
    void setUp() throws Exception {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();

        // Set up some rooms
        unbookedRoom = addRoom(new Room("a", "", 5, BigDecimal.TEN));
        occupiedRoom = addRoom(new Room("b", "", 4, BigDecimal.ONE));
        occupiedRoom2 = addRoom(new Room("c", "", 3, BigDecimal.TWO));

        // cheat by adding directly to repository. We test the controller later.
        booking1 = bookingRepository.save(new Booking(LocalDate.of(1900,1,1), LocalDate.of(3000, 1, 1), occupiedRoom));
        booking2 = bookingRepository.save(new Booking(currentDate, currentDate, occupiedRoom2));
    }

    @Test
    void testFindAvailableRooms() throws Exception {
        var availableRooms = getRooms(currentDate, currentDate.plusDays(5), 3);
        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.contains(unbookedRoom), "Expected room not found when searching available rooms.");
    }

    @Test
    void testNoBookingsFoundOutsideOfBookedDateRanges() throws Exception {
        var bookings = getBookings(LocalDate.of(3001, 1, 1), LocalDate.of(3002,1,1));
        assertEquals(0, bookings.size());
    }

    @Test
    void testExpectedBookingAreFound() throws Exception {
        var bookings = getBookings(currentDate, currentDate);
        assertEquals(2, bookings.size());
        assertTrue(bookings.containsAll(List.of(booking1, booking2)));
    }

    @Test
    void testInvalidBookingIsRejected() throws Exception {
        var invalidBooking = new Booking(currentDate, currentDate, occupiedRoom);
        var unsuccessfulBookingResponse = createBooking(invalidBooking, BAD_REQUEST);
        assertFalse(unsuccessfulBookingResponse.accepted());
        assertEquals(ROOM_UNAVAILABLE_MSG, unsuccessfulBookingResponse.comment());
        assertTrue(getAllBookings().stream().noneMatch(booking -> booking.equivalent(invalidBooking)), "Invalid booking should not be found in bookings.");
    }

    @Test
    void testValidBookingIsAccepted() throws Exception {
        var validBooking = new Booking(currentDate.plusDays(30), currentDate.plusDays(31), occupiedRoom2);
        var successfulBookingResponse = createBooking(validBooking, CREATED);
        assertTrue(successfulBookingResponse.accepted());
        assertTrue(getAllBookings().stream().anyMatch(booking -> booking.equivalent(validBooking)));
    }

    @Test
    void testCancelBooking() throws Exception {
        var validBooking = new Booking(currentDate.plusDays(30), currentDate.plusDays(31), occupiedRoom2);
        var successfulBookingResponse = createBooking(validBooking, CREATED);
        cancelBooking(successfulBookingResponse.id());
        assertFalse(getAllBookings().stream().anyMatch(booking -> booking.equivalent(validBooking)));
    }


    private void cancelBooking(String bookingId) throws Exception {
        var jsonResult = mockMvc.perform(MockMvcRequestBuilders.delete("/booking/{id}", bookingId))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn().getResponse().getContentAsString();
    }

    private Room addRoom(Room room) throws Exception {
        var roomJson = mockMvc.perform(MockMvcRequestBuilders.post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(roomJson, Room.class);
    }

    private List<Booking> getAllBookings() throws Exception {
        var jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/booking")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(jsonResult, new TypeReference<>() {});
    }

    private List<Booking> getBookings(LocalDate startDate, LocalDate endDate) throws Exception {
        var jsonResult = mockMvc.perform(MockMvcRequestBuilders.get("/booking")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(jsonResult, new TypeReference<>() {});
    }

    private List<Room> getRooms(LocalDate startDate, LocalDate endDate, int numberOfBeds) throws Exception {
        // Perform a GET request to your controller method
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/room/available")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .param("numberOfBeds", String.valueOf(numberOfBeds))
                        .contentType(APPLICATION_JSON))
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(jsonResponse, new TypeReference<>() {});
    }

    private CreateBookingResponse createBooking(Booking booking, HttpStatus expectedStatus) throws Exception {
        var jsonString = mockMvc.perform(MockMvcRequestBuilders.post("/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(jsonString, CreateBookingResponse.class);
    }
}