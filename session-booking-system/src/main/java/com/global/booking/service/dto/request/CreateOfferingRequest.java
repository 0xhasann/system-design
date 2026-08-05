package com.global.booking.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfferingRequest {

    @NotNull(message = "Course Id is required")
    private Long courseId;

    @NotNull(message = "Teacher Id is required")
    private Long teacherId;

    @NotBlank(message = "Offering name is required")
    private String name;
}
