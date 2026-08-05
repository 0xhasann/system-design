package com.global.booking.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferingResponse {

    private Long offeringId;

    private String offeringName;

    private Long courseId;

    private String courseName;

    private Long teacherId;

    private String teacherName;

    private List<SessionResponse> sessions;
}
