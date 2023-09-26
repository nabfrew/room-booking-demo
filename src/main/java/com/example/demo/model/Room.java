package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private int beds;

    @Column
    private BigDecimal price;

    // for JPA only, no use
    public Room() {
    }
    public Room(String id, String name, String description, int beds, BigDecimal price) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return true if all fields except id are equal
     */
    public boolean equivalent(Room other) {
        return price.compareTo(other.getPrice()) == 0
        && other.getName().equals(name)
        && other.getDescription().equals(description)
        && other.getBeds() == beds;
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
