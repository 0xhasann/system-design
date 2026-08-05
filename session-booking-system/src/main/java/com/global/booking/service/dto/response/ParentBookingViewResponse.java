package com.global.booking.service.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentBookingViewResponse {

    private Long bookingId;

    private String offeringName;

    private List<SessionResponse> sessions;

}
