package com.global.booking.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionResponse {

    private Long sessionId;

    private Long offeringId;

    private String startTimeUtc;

    private String endTimeUtc;
}
