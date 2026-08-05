package com.shurutech.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableAvailabilityDto {

    private Long tableId;

    private String tableName;

    // Total seats
    private Integer capacity;

    // Already reserved seats
    private Integer bookedSeats;

    // Remaining seats
    private Integer remainingSeats;
}
