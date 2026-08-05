package com.shurutech.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shurutech.reservation.entity.Reservation;

@Repository
public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    // Fetch reservations for a table/date/slot
    List<Reservation> findByTableIdAndReservationDateAndSlot(
            Long tableId,
            LocalDate date,
            LocalTime slot);

    // Fetch all reservations for a date/slot
    List<Reservation> findByReservationDateAndSlot(
            LocalDate date,
            LocalTime slot);
}
