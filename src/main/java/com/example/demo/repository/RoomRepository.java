package com.example.demo.repository;

import com.example.demo.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Spring Data JPA creates CRUD implementation at runtime automatically.
public interface RoomRepository extends JpaRepository<Room, Long> {

    //ist<Room> findByName(String name);

    // Custom query
    //@Query("SELECT b FROM Room b WHERE b.publishDate > :date")
    //List<Room> findByPublishedDateAfter(@Param("date") LocalDate date);

}
