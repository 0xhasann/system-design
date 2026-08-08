
package com.global.booking.service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Session extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id")
    private Offering offering;

    LocalDateTime startTimeUtc;

    LocalDateTime endTimeUtc;
}
