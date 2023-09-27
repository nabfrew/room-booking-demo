package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

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
        this(name, description, beds, price);
        this.id = id;
    }

    public Room(String name, String description, int beds, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.beds = beds;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBeds() {
        return beds;
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

    /**
     * @return true if all fields except id are equal
     */
    public boolean equivalent(Room other) {
        return price.compareTo(other.getPrice()) == 0 // BigDecimal is pain for equality.
        && other.getName().equals(name)
        && other.getDescription().equals(description)
        && other.getBeds() == beds;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Room otherRoom)) {
            return false;
        }
        return id.equals(otherRoom.getId())
                && equivalent(otherRoom);
    }

    @Override
    public int hashCode() {
        // Strip traililng zeros should ensure numerically equal BigDecimals get equal hash codes.
        return Objects.hash(price.stripTrailingZeros(), id, beds, name, description);
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
