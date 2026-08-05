package com.global.booking.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.global.booking.service.booking.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
