package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Check(constraints = "start_date <= end_date")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn
    private Room room;

    @Column
    private LocalDate startDate;

    // 'end' is an sql keywords, wreaks havoc if used on without 'Date'.
    @Column
    private LocalDate endDate;

    public Booking() {
    }

    public Booking(LocalDate startDate, LocalDate endDate, Room room) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate end) {
        this.endDate = end;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate start) {
        this.startDate = start;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    /**
     * @return true if start, end, & room match. Id excluded.
     */
    public boolean equivalent(Booking other) {
        return startDate.equals(other.startDate)
                && endDate.equals(other.endDate)
                && room.equals(other.getRoom());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Booking otherBooking)) {
            return false;
        }

        return id.equals(otherBooking.id)
                && equivalent(otherBooking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, room);
    }
}
