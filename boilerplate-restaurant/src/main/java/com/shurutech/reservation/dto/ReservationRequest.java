package com.shurutech.reservation.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationRequest {

    private Long tableId;

    private LocalDate date;

    private LocalTime slot;

    private Integer guestCount;

    private String customerName;

    private String customerEmail;

}
