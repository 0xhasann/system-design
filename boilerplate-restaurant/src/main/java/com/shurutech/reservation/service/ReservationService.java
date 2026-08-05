package com.shurutech.reservation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shurutech.reservation.dto.ReservationRequest;
import com.shurutech.reservation.dto.ReservationResponse;
import com.shurutech.reservation.entity.Reservation;
import com.shurutech.reservation.entity.RestaurantTable;
import com.shurutech.reservation.repository.ReservationRepository;
import com.shurutech.reservation.repository.TableRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

        private final TableRepository tableRepository;
        private final ReservationRepository reservationRepository;

        @Transactional
        public ReservationResponse book(
                        ReservationRequest request) {

                RestaurantTable table = tableRepository.findById(
                                request.getTableId())
                                .orElseThrow();

                List<Reservation> reservations = reservationRepository
                                .findByTableIdAndReservationDateAndSlot(
                                                request.getTableId(),
                                                request.getDate(),
                                                request.getSlot());

                // Calculate occupied seats
                int bookedSeats = reservations.stream()
                                .mapToInt(Reservation::getGuestCount)
                                .sum();

                int remainingSeats = table.getCapacity() - bookedSeats;

                // Prevent overbooking
                if (remainingSeats < request.getGuestCount()) {
                        throw new RuntimeException(
                                        "Not enough seats available");
                }

                Reservation reservation = new Reservation();

                reservation.setTable(table);
                reservation.setReservationDate(
                                request.getDate());
                reservation.setSlot(
                                request.getSlot());
                reservation.setGuestCount(
                                request.getGuestCount());
                reservation.setCustomerName(
                                request.getCustomerName());
                reservation.setCustomerEmail(
                                request.getCustomerEmail());

                Reservation saved = reservationRepository.save(
                                reservation);

                ReservationResponse response = new ReservationResponse();

                response.setReservationId(saved.getId());
                response.setMessage(
                                "Reservation successful");

                return response;
        }
}
