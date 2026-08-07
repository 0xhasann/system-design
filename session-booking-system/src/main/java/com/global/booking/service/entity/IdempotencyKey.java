
package com.global.booking.service.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKey extends BaseEntity {

    @Column(unique = true)
    String idempotencyKey;

    @Lob
    String responseJson;

    private Integer statusCode;
}
