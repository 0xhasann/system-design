package com.shurutech.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shurutech.reservation.dto.AvailabilityResponse;
import com.shurutech.reservation.dto.TableAvailabilityDto;
import com.shurutech.reservation.entity.Reservation;
import com.shurutech.reservation.entity.RestaurantTable;
import com.shurutech.reservation.repository.ReservationRepository;
import com.shurutech.reservation.repository.TableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public List<AvailabilityResponse> getAvailability(
            LocalDate date,
            int guestCount) {

        List<LocalTime> slots = List.of(
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                LocalTime.of(21, 0));

        List<RestaurantTable> tables = tableRepository.findAll();

        List<AvailabilityResponse> result = new ArrayList<>();

        for (LocalTime slot : slots) {

            List<TableAvailabilityDto> availableTables = new ArrayList<>();

            for (RestaurantTable table : tables) {

                List<Reservation> reservations = reservationRepository
                        .findByTableIdAndReservationDateAndSlot(
                                table.getId(),
                                date,
                                slot);

                // Sum already booked seats
                int bookedSeats = reservations.stream()
                        .mapToInt(Reservation::getGuestCount)
                        .sum();

                int remainingSeats = table.getCapacity() - bookedSeats;

                // Advanced requirement:
                // Include partially filled tables
                if (remainingSeats >= guestCount) {

                    TableAvailabilityDto dto = new TableAvailabilityDto();

                    dto.setTableName(table.getTableName());
                    dto.setTableId(table.getId());
                    dto.setCapacity(table.getCapacity());
                    dto.setBookedSeats(bookedSeats);
                    dto.setRemainingSeats(remainingSeats);

                    availableTables.add(dto);
                }
            }

            AvailabilityResponse response = AvailabilityResponse.builder()
                    .slot(slot)
                    .availableTables(availableTables)
                    .build();
            result.add(response);
        }

        return result;
    }
}
