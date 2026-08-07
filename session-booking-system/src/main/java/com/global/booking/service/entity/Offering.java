
package com.global.booking.service.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Offering extends BaseEntity {

    String name;

    @ManyToOne
    Teacher teacher;

    @ManyToOne
    Course course;

    @OneToMany(mappedBy = "offering")
    List<Session> sessions;

    @Version
    Long version;
}
