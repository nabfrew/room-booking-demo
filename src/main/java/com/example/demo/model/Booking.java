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

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn
    private Room room;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    public Booking() {
    }

    public Booking(LocalDate startDate, LocalDate endDate, Room room) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }

    @Check(constraints = "start_date < end_date")
    private void validRange() {
        if (startDate == null || endDate == null || startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
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
}
