package com.global.booking.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.global.booking.service.booking.entity.Session;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByOfferingId(Long offeringId);
}
