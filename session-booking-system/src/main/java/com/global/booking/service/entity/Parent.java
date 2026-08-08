
package com.global.booking.service.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parents")
public class Parent extends BaseEntity {

    String name;

    @Column(nullable = false)
    private String timezone;
}
