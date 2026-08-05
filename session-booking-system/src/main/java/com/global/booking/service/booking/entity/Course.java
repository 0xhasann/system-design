
package com.global.booking.service.booking.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Course extends BaseEntity {

    String name;
}
