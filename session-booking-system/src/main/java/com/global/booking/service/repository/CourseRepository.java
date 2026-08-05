package com.global.booking.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.global.booking.service.booking.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
