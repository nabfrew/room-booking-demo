package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int beds;

    @Column
    private BigDecimal price;

    @Convert(converter = DateRangeListConverter.class)
    @Column(name = "dates_available", columnDefinition = "int8range")
    private List<DateRange> datesAvailable = new ArrayList<>();

    // for JPA only, no use
    public Room() {
    }
    public Room(long id, String name, String description, int beds, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.beds = beds;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public List<DateRange> getDatesAvailable() {
        return datesAvailable;
    }

    public void setDatesAvailable(List<DateRange> datesAvailable) {
        this.datesAvailable = datesAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", title='" + name + '\'' +
                ", price=" + price +
                ", beds=" + beds +
                ", publishDate=" + description +
                '}';
    }

}
