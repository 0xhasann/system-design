package com.shurutech.reservation.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which table is booked
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    private LocalDate reservationDate;

    private LocalTime slot;

    private Integer guestCount;

    private String customerName;
    private String customerEmail;
}
