package com.shurutech.reservation.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shurutech.reservation.dto.AvailabilityResponse;
import com.shurutech.reservation.dto.ReservationRequest;
import com.shurutech.reservation.dto.ReservationResponse;
import com.shurutech.reservation.service.AvailabilityService;
import com.shurutech.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final AvailabilityService availabilityService;

    private final ReservationService reservationService;

    // View available slots
    @GetMapping("/availability")
    public List<AvailabilityResponse> getAvailability(
            @RequestParam LocalDate date,
            @RequestParam Integer guests) {

        return availabilityService
                .getAvailability(date, guests);
    }

    // Book a table
    @PostMapping("/book")
    public ReservationResponse book(
            @RequestBody ReservationRequest request) {

        return reservationService.book(request);
    }
}
