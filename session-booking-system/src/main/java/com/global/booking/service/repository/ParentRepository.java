package com.global.booking.service.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.global.booking.service.booking.entity.Parent;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Parent p
            WHERE p.id = :parentId
            """)
    Optional<Parent> lockParent(Long parentId);
}
