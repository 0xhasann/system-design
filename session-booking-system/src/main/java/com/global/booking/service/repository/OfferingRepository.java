package com.global.booking.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.global.booking.service.entity.Offering;

import java.util.List;

public interface OfferingRepository extends JpaRepository<Offering, Long> {

    List<Offering> findByTeacherId(Long teacherId);

    @EntityGraph(attributePaths = {
            "course",
            "teacher",
            "sessions"
    })
    List<Offering> findAll();
}
