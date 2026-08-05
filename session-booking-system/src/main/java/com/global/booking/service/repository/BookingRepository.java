package com.global.booking.service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.global.booking.service.booking.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {
            "offering",
            "offering.sessions"
    })
    List<Booking> findByParentId(Long parentId);

    boolean existsByParentIdAndOfferingId(
            Long parentId,
            Long offeringId);

    @Query("""
            SELECT COUNT(s)
            FROM Session s
            JOIN Booking b
                ON b.offering.id = s.offering.id

            WHERE b.parent.id = :parentId

            AND s.startTimeUtc < :newEnd

            AND s.endTimeUtc > :newStart
            """)
    long countOverlappingSessions(
            Long parentId,
            LocalDateTime newStart,
            LocalDateTime newEnd);
}
