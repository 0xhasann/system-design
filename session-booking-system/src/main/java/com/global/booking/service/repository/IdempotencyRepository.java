package com.global.booking.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.global.booking.service.booking.entity.IdempotencyKey;

import java.util.Optional;

public interface IdempotencyRepository
        extends JpaRepository<IdempotencyKey, Long> {

    Optional<IdempotencyKey> findByIdempotencyKey(
            String idempotencyKey);
}
