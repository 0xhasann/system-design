package com.global.booking.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOfferingRequest {

    @NotNull(message = "Parent Id is required")
    private Long parentId;

    @NotNull(message = "Offering Id is required")
    private Long offeringId;
}
